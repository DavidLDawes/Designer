import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { Fitting } from 'app/entities/enumerations/fitting.model';
import { FittingsService } from '../service/fittings.service';
import { IFittings } from '../fittings.model';
import { FittingsFormGroup, FittingsFormService } from './fittings-form.service';

@Component({
  selector: 'jhi-fittings-update',
  templateUrl: './fittings-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FittingsUpdateComponent implements OnInit {
  isSaving = false;
  fittings: IFittings | null = null;
  fittingValues = Object.keys(Fitting);

  shipsSharedCollection: IShips[] = [];

  protected fittingsService = inject(FittingsService);
  protected fittingsFormService = inject(FittingsFormService);
  protected shipsService = inject(ShipsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FittingsFormGroup = this.fittingsFormService.createFittingsFormGroup();

  compareShips = (o1: IShips | null, o2: IShips | null): boolean => this.shipsService.compareShips(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fittings }) => {
      this.fittings = fittings;
      if (fittings) {
        this.updateForm(fittings);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fittings = this.fittingsFormService.getFittings(this.editForm);
    if (fittings.id !== null) {
      this.subscribeToSaveResponse(this.fittingsService.update(fittings));
    } else {
      this.subscribeToSaveResponse(this.fittingsService.create(fittings));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFittings>>): void {
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

  protected updateForm(fittings: IFittings): void {
    this.fittings = fittings;
    this.fittingsFormService.resetForm(this.editForm, fittings);

    this.shipsSharedCollection = this.shipsService.addShipsToCollectionIfMissing<IShips>(this.shipsSharedCollection, fittings.ships);
  }

  protected loadRelationshipsOptions(): void {
    this.shipsService
      .query()
      .pipe(map((res: HttpResponse<IShips[]>) => res.body ?? []))
      .pipe(map((ships: IShips[]) => this.shipsService.addShipsToCollectionIfMissing<IShips>(ships, this.fittings?.ships)))
      .subscribe((ships: IShips[]) => (this.shipsSharedCollection = ships));
  }
}
