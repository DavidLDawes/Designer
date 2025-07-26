import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { Weapon } from 'app/entities/enumerations/weapon.model';
import { WeaponsService } from '../service/weapons.service';
import { IWeapons } from '../weapons.model';
import { WeaponsFormGroup, WeaponsFormService } from './weapons-form.service';

@Component({
  selector: 'jhi-weapons-update',
  templateUrl: './weapons-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WeaponsUpdateComponent implements OnInit {
  isSaving = false;
  weapons: IWeapons | null = null;
  weaponValues = Object.keys(Weapon);

  shipsSharedCollection: IShips[] = [];

  protected weaponsService = inject(WeaponsService);
  protected weaponsFormService = inject(WeaponsFormService);
  protected shipsService = inject(ShipsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WeaponsFormGroup = this.weaponsFormService.createWeaponsFormGroup();

  compareShips = (o1: IShips | null, o2: IShips | null): boolean => this.shipsService.compareShips(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weapons }) => {
      this.weapons = weapons;
      if (weapons) {
        this.updateForm(weapons);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const weapons = this.weaponsFormService.getWeapons(this.editForm);
    if (weapons.id !== null) {
      this.subscribeToSaveResponse(this.weaponsService.update(weapons));
    } else {
      this.subscribeToSaveResponse(this.weaponsService.create(weapons));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeapons>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(weapons: IWeapons): void {
    this.weapons = weapons;
    this.weaponsFormService.resetForm(this.editForm, weapons);

    this.shipsSharedCollection = this.shipsService.addShipsToCollectionIfMissing<IShips>(this.shipsSharedCollection, weapons.ships);
  }

  protected loadRelationshipsOptions(): void {
    this.shipsService
      .query()
      .pipe(map((res: HttpResponse<IShips[]>) => res.body ?? []))
      .pipe(map((ships: IShips[]) => this.shipsService.addShipsToCollectionIfMissing<IShips>(ships, this.weapons?.ships)))
      .subscribe((ships: IShips[]) => (this.shipsSharedCollection = ships));
  }
}
