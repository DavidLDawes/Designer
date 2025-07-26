import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { Vehicle } from 'app/entities/enumerations/vehicle.model';
import { VehiclesService } from '../service/vehicles.service';
import { IVehicles } from '../vehicles.model';
import { VehiclesFormGroup, VehiclesFormService } from './vehicles-form.service';

@Component({
  selector: 'jhi-vehicles-update',
  templateUrl: './vehicles-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehiclesUpdateComponent implements OnInit {
  isSaving = false;
  vehicles: IVehicles | null = null;
  vehicleValues = Object.keys(Vehicle);

  shipsSharedCollection: IShips[] = [];

  protected vehiclesService = inject(VehiclesService);
  protected vehiclesFormService = inject(VehiclesFormService);
  protected shipsService = inject(ShipsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehiclesFormGroup = this.vehiclesFormService.createVehiclesFormGroup();

  compareShips = (o1: IShips | null, o2: IShips | null): boolean => this.shipsService.compareShips(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicles }) => {
      this.vehicles = vehicles;
      if (vehicles) {
        this.updateForm(vehicles);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicles = this.vehiclesFormService.getVehicles(this.editForm);
    if (vehicles.id !== null) {
      this.subscribeToSaveResponse(this.vehiclesService.update(vehicles));
    } else {
      this.subscribeToSaveResponse(this.vehiclesService.create(vehicles));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicles>>): void {
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

  protected updateForm(vehicles: IVehicles): void {
    this.vehicles = vehicles;
    this.vehiclesFormService.resetForm(this.editForm, vehicles);

    this.shipsSharedCollection = this.shipsService.addShipsToCollectionIfMissing<IShips>(this.shipsSharedCollection, vehicles.ships);
  }

  protected loadRelationshipsOptions(): void {
    this.shipsService
      .query()
      .pipe(map((res: HttpResponse<IShips[]>) => res.body ?? []))
      .pipe(map((ships: IShips[]) => this.shipsService.addShipsToCollectionIfMissing<IShips>(ships, this.vehicles?.ships)))
      .subscribe((ships: IShips[]) => (this.shipsSharedCollection = ships));
  }
}
