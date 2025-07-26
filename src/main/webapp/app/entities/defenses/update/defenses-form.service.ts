import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDefenses, NewDefenses } from '../defenses.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDefenses for edit and NewDefensesFormGroupInput for create.
 */
type DefensesFormGroupInput = IDefenses | PartialWithRequiredKeyOf<NewDefenses>;

type DefensesFormDefaults = Pick<NewDefenses, 'id' | 'armored'>;

type DefensesFormGroupContent = {
  id: FormControl<IDefenses['id'] | NewDefenses['id']>;
  defense: FormControl<IDefenses['defense']>;
  dShipId: FormControl<IDefenses['dShipId']>;
  battery: FormControl<IDefenses['battery']>;
  count: FormControl<IDefenses['count']>;
  mass: FormControl<IDefenses['mass']>;
  cost: FormControl<IDefenses['cost']>;
  armored: FormControl<IDefenses['armored']>;
  ships: FormControl<IDefenses['ships']>;
};

export type DefensesFormGroup = FormGroup<DefensesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DefensesFormService {
  createDefensesFormGroup(defenses: DefensesFormGroupInput = { id: null }): DefensesFormGroup {
    const defensesRawValue = {
      ...this.getFormDefaults(),
      ...defenses,
    };
    return new FormGroup<DefensesFormGroupContent>({
      id: new FormControl(
        { value: defensesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      defense: new FormControl(defensesRawValue.defense, {
        validators: [Validators.required],
      }),
      dShipId: new FormControl(defensesRawValue.dShipId, {
        validators: [Validators.required],
      }),
      battery: new FormControl(defensesRawValue.battery, {
        validators: [Validators.required],
      }),
      count: new FormControl(defensesRawValue.count, {
        validators: [Validators.required],
      }),
      mass: new FormControl(defensesRawValue.mass, {
        validators: [Validators.required],
      }),
      cost: new FormControl(defensesRawValue.cost, {
        validators: [Validators.required],
      }),
      armored: new FormControl(defensesRawValue.armored, {
        validators: [Validators.required],
      }),
      ships: new FormControl(defensesRawValue.ships),
    });
  }

  getDefenses(form: DefensesFormGroup): IDefenses | NewDefenses {
    return form.getRawValue() as IDefenses | NewDefenses;
  }

  resetForm(form: DefensesFormGroup, defenses: DefensesFormGroupInput): void {
    const defensesRawValue = { ...this.getFormDefaults(), ...defenses };
    form.reset(
      {
        ...defensesRawValue,
        id: { value: defensesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DefensesFormDefaults {
    return {
      id: null,
      armored: false,
    };
  }
}
