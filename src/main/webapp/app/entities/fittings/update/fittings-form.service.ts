import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFittings, NewFittings } from '../fittings.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFittings for edit and NewFittingsFormGroupInput for create.
 */
type FittingsFormGroupInput = IFittings | PartialWithRequiredKeyOf<NewFittings>;

type FittingsFormDefaults = Pick<NewFittings, 'id' | 'armored'>;

type FittingsFormGroupContent = {
  id: FormControl<IFittings['id'] | NewFittings['id']>;
  fitting: FormControl<IFittings['fitting']>;
  fShipId: FormControl<IFittings['fShipId']>;
  count: FormControl<IFittings['count']>;
  mass: FormControl<IFittings['mass']>;
  cost: FormControl<IFittings['cost']>;
  armored: FormControl<IFittings['armored']>;
  ships: FormControl<IFittings['ships']>;
};

export type FittingsFormGroup = FormGroup<FittingsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FittingsFormService {
  createFittingsFormGroup(fittings: FittingsFormGroupInput = { id: null }): FittingsFormGroup {
    const fittingsRawValue = {
      ...this.getFormDefaults(),
      ...fittings,
    };
    return new FormGroup<FittingsFormGroupContent>({
      id: new FormControl(
        { value: fittingsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fitting: new FormControl(fittingsRawValue.fitting, {
        validators: [Validators.required],
      }),
      fShipId: new FormControl(fittingsRawValue.fShipId, {
        validators: [Validators.required],
      }),
      count: new FormControl(fittingsRawValue.count, {
        validators: [Validators.required],
      }),
      mass: new FormControl(fittingsRawValue.mass, {
        validators: [Validators.required],
      }),
      cost: new FormControl(fittingsRawValue.cost, {
        validators: [Validators.required],
      }),
      armored: new FormControl(fittingsRawValue.armored, {
        validators: [Validators.required],
      }),
      ships: new FormControl(fittingsRawValue.ships),
    });
  }

  getFittings(form: FittingsFormGroup): IFittings | NewFittings {
    return form.getRawValue() as IFittings | NewFittings;
  }

  resetForm(form: FittingsFormGroup, fittings: FittingsFormGroupInput): void {
    const fittingsRawValue = { ...this.getFormDefaults(), ...fittings };
    form.reset(
      {
        ...fittingsRawValue,
        id: { value: fittingsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FittingsFormDefaults {
    return {
      id: null,
      armored: false,
    };
  }
}
