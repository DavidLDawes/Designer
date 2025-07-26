import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWeapons, NewWeapons } from '../weapons.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWeapons for edit and NewWeaponsFormGroupInput for create.
 */
type WeaponsFormGroupInput = IWeapons | PartialWithRequiredKeyOf<NewWeapons>;

type WeaponsFormDefaults = Pick<NewWeapons, 'id' | 'armored'>;

type WeaponsFormGroupContent = {
  id: FormControl<IWeapons['id'] | NewWeapons['id']>;
  weapon: FormControl<IWeapons['weapon']>;
  wShipId: FormControl<IWeapons['wShipId']>;
  battery: FormControl<IWeapons['battery']>;
  count: FormControl<IWeapons['count']>;
  mass: FormControl<IWeapons['mass']>;
  cost: FormControl<IWeapons['cost']>;
  armored: FormControl<IWeapons['armored']>;
  ships: FormControl<IWeapons['ships']>;
};

export type WeaponsFormGroup = FormGroup<WeaponsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WeaponsFormService {
  createWeaponsFormGroup(weapons: WeaponsFormGroupInput = { id: null }): WeaponsFormGroup {
    const weaponsRawValue = {
      ...this.getFormDefaults(),
      ...weapons,
    };
    return new FormGroup<WeaponsFormGroupContent>({
      id: new FormControl(
        { value: weaponsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      weapon: new FormControl(weaponsRawValue.weapon, {
        validators: [Validators.required],
      }),
      wShipId: new FormControl(weaponsRawValue.wShipId, {
        validators: [Validators.required],
      }),
      battery: new FormControl(weaponsRawValue.battery, {
        validators: [Validators.required],
      }),
      count: new FormControl(weaponsRawValue.count, {
        validators: [Validators.required],
      }),
      mass: new FormControl(weaponsRawValue.mass, {
        validators: [Validators.required],
      }),
      cost: new FormControl(weaponsRawValue.cost, {
        validators: [Validators.required],
      }),
      armored: new FormControl(weaponsRawValue.armored, {
        validators: [Validators.required],
      }),
      ships: new FormControl(weaponsRawValue.ships),
    });
  }

  getWeapons(form: WeaponsFormGroup): IWeapons | NewWeapons {
    return form.getRawValue() as IWeapons | NewWeapons;
  }

  resetForm(form: WeaponsFormGroup, weapons: WeaponsFormGroupInput): void {
    const weaponsRawValue = { ...this.getFormDefaults(), ...weapons };
    form.reset(
      {
        ...weaponsRawValue,
        id: { value: weaponsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WeaponsFormDefaults {
    return {
      id: null,
      armored: false,
    };
  }
}
