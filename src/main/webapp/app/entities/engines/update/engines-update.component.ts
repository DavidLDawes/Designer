import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { Engine } from 'app/entities/enumerations/engine.model';
import { TL } from 'app/entities/enumerations/tl.model';
import { EnginesService } from '../service/engines.service';
import { IEngines } from '../engines.model';
import { EnginesFormGroup, EnginesFormService } from './engines-form.service';

@Component({
  selector: 'jhi-engines-update',
  templateUrl: './engines-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnginesUpdateComponent implements OnInit {
  isSaving = false;
  engines: IEngines | null = null;
  engineValues = Object.keys(Engine);
  tLValues = Object.keys(TL);

  shipsSharedCollection: IShips[] = [];

  protected enginesService = inject(EnginesService);
  protected enginesFormService = inject(EnginesFormService);
  protected shipsService = inject(ShipsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EnginesFormGroup = this.enginesFormService.createEnginesFormGroup();

  compareShips = (o1: IShips | null, o2: IShips | null): boolean => this.shipsService.compareShips(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ engines }) => {
      this.engines = engines;
      if (engines) {
        this.updateForm(engines);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const engines = this.enginesFormService.getEngines(this.editForm);
    if (engines.id !== null) {
      this.subscribeToSaveResponse(this.enginesService.update(engines));
    } else {
      this.subscribeToSaveResponse(this.enginesService.create(engines));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEngines>>): void {
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

  protected updateForm(engines: IEngines): void {
    this.engines = engines;
    this.enginesFormService.resetForm(this.editForm, engines);

    this.shipsSharedCollection = this.shipsService.addShipsToCollectionIfMissing<IShips>(this.shipsSharedCollection, engines.ships);
  }

  protected loadRelationshipsOptions(): void {
    this.shipsService
      .query()
      .pipe(map((res: HttpResponse<IShips[]>) => res.body ?? []))
      .pipe(map((ships: IShips[]) => this.shipsService.addShipsToCollectionIfMissing<IShips>(ships, this.engines?.ships)))
      .subscribe((ships: IShips[]) => (this.shipsSharedCollection = ships));
  }
}
