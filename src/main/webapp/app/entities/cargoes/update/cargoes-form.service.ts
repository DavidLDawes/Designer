import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICargoes, NewCargoes } from '../cargoes.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICargoes for edit and NewCargoesFormGroupInput for create.
 */
type CargoesFormGroupInput = ICargoes | PartialWithRequiredKeyOf<NewCargoes>;

type CargoesFormDefaults = Pick<NewCargoes, 'id' | 'armored'>;

type CargoesFormGroupContent = {
  id: FormControl<ICargoes['id'] | NewCargoes['id']>;
  cargo: FormControl<ICargoes['cargo']>;
  cShipId: FormControl<ICargoes['cShipId']>;
  mass: FormControl<ICargoes['mass']>;
  cost: FormControl<ICargoes['cost']>;
  armored: FormControl<ICargoes['armored']>;
  ships: FormControl<ICargoes['ships']>;
};

export type CargoesFormGroup = FormGroup<CargoesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CargoesFormService {
  createCargoesFormGroup(cargoes: CargoesFormGroupInput = { id: null }): CargoesFormGroup {
    const cargoesRawValue = {
      ...this.getFormDefaults(),
      ...cargoes,
    };
    return new FormGroup<CargoesFormGroupContent>({
      id: new FormControl(
        { value: cargoesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cargo: new FormControl(cargoesRawValue.cargo, {
        validators: [Validators.required],
      }),
      cShipId: new FormControl(cargoesRawValue.cShipId, {
        validators: [Validators.required],
      }),
      mass: new FormControl(cargoesRawValue.mass, {
        validators: [Validators.required],
      }),
      cost: new FormControl(cargoesRawValue.cost, {
        validators: [Validators.required],
      }),
      armored: new FormControl(cargoesRawValue.armored, {
        validators: [Validators.required],
      }),
      ships: new FormControl(cargoesRawValue.ships),
    });
  }

  getCargoes(form: CargoesFormGroup): ICargoes | NewCargoes {
    return form.getRawValue() as ICargoes | NewCargoes;
  }

  resetForm(form: CargoesFormGroup, cargoes: CargoesFormGroupInput): void {
    const cargoesRawValue = { ...this.getFormDefaults(), ...cargoes };
    form.reset(
      {
        ...cargoesRawValue,
        id: { value: cargoesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CargoesFormDefaults {
    return {
      id: null,
      armored: false,
    };
  }
}
