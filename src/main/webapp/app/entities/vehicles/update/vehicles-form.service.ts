import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVehicles, NewVehicles } from '../vehicles.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicles for edit and NewVehiclesFormGroupInput for create.
 */
type VehiclesFormGroupInput = IVehicles | PartialWithRequiredKeyOf<NewVehicles>;

type VehiclesFormDefaults = Pick<NewVehicles, 'id' | 'armored' | 'repairBay'>;

type VehiclesFormGroupContent = {
  id: FormControl<IVehicles['id'] | NewVehicles['id']>;
  vehicle: FormControl<IVehicles['vehicle']>;
  vShipId: FormControl<IVehicles['vShipId']>;
  mass: FormControl<IVehicles['mass']>;
  cost: FormControl<IVehicles['cost']>;
  armored: FormControl<IVehicles['armored']>;
  repairBay: FormControl<IVehicles['repairBay']>;
  ships: FormControl<IVehicles['ships']>;
};

export type VehiclesFormGroup = FormGroup<VehiclesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehiclesFormService {
  createVehiclesFormGroup(vehicles: VehiclesFormGroupInput = { id: null }): VehiclesFormGroup {
    const vehiclesRawValue = {
      ...this.getFormDefaults(),
      ...vehicles,
    };
    return new FormGroup<VehiclesFormGroupContent>({
      id: new FormControl(
        { value: vehiclesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      vehicle: new FormControl(vehiclesRawValue.vehicle, {
        validators: [Validators.required],
      }),
      vShipId: new FormControl(vehiclesRawValue.vShipId, {
        validators: [Validators.required],
      }),
      mass: new FormControl(vehiclesRawValue.mass, {
        validators: [Validators.required],
      }),
      cost: new FormControl(vehiclesRawValue.cost, {
        validators: [Validators.required],
      }),
      armored: new FormControl(vehiclesRawValue.armored, {
        validators: [Validators.required],
      }),
      repairBay: new FormControl(vehiclesRawValue.repairBay, {
        validators: [Validators.required],
      }),
      ships: new FormControl(vehiclesRawValue.ships),
    });
  }

  getVehicles(form: VehiclesFormGroup): IVehicles | NewVehicles {
    return form.getRawValue() as IVehicles | NewVehicles;
  }

  resetForm(form: VehiclesFormGroup, vehicles: VehiclesFormGroupInput): void {
    const vehiclesRawValue = { ...this.getFormDefaults(), ...vehicles };
    form.reset(
      {
        ...vehiclesRawValue,
        id: { value: vehiclesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VehiclesFormDefaults {
    return {
      id: null,
      armored: false,
      repairBay: false,
    };
  }
}
