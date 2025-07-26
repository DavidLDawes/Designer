import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { Defense } from 'app/entities/enumerations/defense.model';
import { DefensesService } from '../service/defenses.service';
import { IDefenses } from '../defenses.model';
import { DefensesFormGroup, DefensesFormService } from './defenses-form.service';

@Component({
  selector: 'jhi-defenses-update',
  templateUrl: './defenses-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DefensesUpdateComponent implements OnInit {
  isSaving = false;
  defenses: IDefenses | null = null;
  defenseValues = Object.keys(Defense);

  shipsSharedCollection: IShips[] = [];

  protected defensesService = inject(DefensesService);
  protected defensesFormService = inject(DefensesFormService);
  protected shipsService = inject(ShipsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DefensesFormGroup = this.defensesFormService.createDefensesFormGroup();

  compareShips = (o1: IShips | null, o2: IShips | null): boolean => this.shipsService.compareShips(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ defenses }) => {
      this.defenses = defenses;
      if (defenses) {
        this.updateForm(defenses);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const defenses = this.defensesFormService.getDefenses(this.editForm);
    if (defenses.id !== null) {
      this.subscribeToSaveResponse(this.defensesService.update(defenses));
    } else {
      this.subscribeToSaveResponse(this.defensesService.create(defenses));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDefenses>>): void {
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

  protected updateForm(defenses: IDefenses): void {
    this.defenses = defenses;
    this.defensesFormService.resetForm(this.editForm, defenses);

    this.shipsSharedCollection = this.shipsService.addShipsToCollectionIfMissing<IShips>(this.shipsSharedCollection, defenses.ships);
  }

  protected loadRelationshipsOptions(): void {
    this.shipsService
      .query()
      .pipe(map((res: HttpResponse<IShips[]>) => res.body ?? []))
      .pipe(map((ships: IShips[]) => this.shipsService.addShipsToCollectionIfMissing<IShips>(ships, this.defenses?.ships)))
      .subscribe((ships: IShips[]) => (this.shipsSharedCollection = ships));
  }
}
