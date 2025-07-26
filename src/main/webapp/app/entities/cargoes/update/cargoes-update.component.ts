import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { Cargo } from 'app/entities/enumerations/cargo.model';
import { CargoesService } from '../service/cargoes.service';
import { ICargoes } from '../cargoes.model';
import { CargoesFormGroup, CargoesFormService } from './cargoes-form.service';

@Component({
  selector: 'jhi-cargoes-update',
  templateUrl: './cargoes-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CargoesUpdateComponent implements OnInit {
  isSaving = false;
  cargoes: ICargoes | null = null;
  cargoValues = Object.keys(Cargo);

  shipsSharedCollection: IShips[] = [];

  protected cargoesService = inject(CargoesService);
  protected cargoesFormService = inject(CargoesFormService);
  protected shipsService = inject(ShipsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CargoesFormGroup = this.cargoesFormService.createCargoesFormGroup();

  compareShips = (o1: IShips | null, o2: IShips | null): boolean => this.shipsService.compareShips(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cargoes }) => {
      this.cargoes = cargoes;
      if (cargoes) {
        this.updateForm(cargoes);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cargoes = this.cargoesFormService.getCargoes(this.editForm);
    if (cargoes.id !== null) {
      this.subscribeToSaveResponse(this.cargoesService.update(cargoes));
    } else {
      this.subscribeToSaveResponse(this.cargoesService.create(cargoes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICargoes>>): void {
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

  protected updateForm(cargoes: ICargoes): void {
    this.cargoes = cargoes;
    this.cargoesFormService.resetForm(this.editForm, cargoes);

    this.shipsSharedCollection = this.shipsService.addShipsToCollectionIfMissing<IShips>(this.shipsSharedCollection, cargoes.ships);
  }

  protected loadRelationshipsOptions(): void {
    this.shipsService
      .query()
      .pipe(map((res: HttpResponse<IShips[]>) => res.body ?? []))
      .pipe(map((ships: IShips[]) => this.shipsService.addShipsToCollectionIfMissing<IShips>(ships, this.cargoes?.ships)))
      .subscribe((ships: IShips[]) => (this.shipsSharedCollection = ships));
  }
}
