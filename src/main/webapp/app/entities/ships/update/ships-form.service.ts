import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IShips, NewShips } from '../ships.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShips for edit and NewShipsFormGroupInput for create.
 */
type ShipsFormGroupInput = IShips | PartialWithRequiredKeyOf<NewShips>;

type ShipsFormDefaults = Pick<NewShips, 'id' | 'capital' | 'military'>;

type ShipsFormGroupContent = {
  id: FormControl<IShips['id'] | NewShips['id']>;
  shipId: FormControl<IShips['shipId']>;
  userId: FormControl<IShips['userId']>;
  tl: FormControl<IShips['tl']>;
  tonnage: FormControl<IShips['tonnage']>;
  conf: FormControl<IShips['conf']>;
  code: FormControl<IShips['code']>;
  hull: FormControl<IShips['hull']>;
  structure: FormControl<IShips['structure']>;
  sections: FormControl<IShips['sections']>;
  capital: FormControl<IShips['capital']>;
  military: FormControl<IShips['military']>;
  user: FormControl<IShips['user']>;
};

export type ShipsFormGroup = FormGroup<ShipsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ShipsFormService {
  createShipsFormGroup(ships: ShipsFormGroupInput = { id: null }): ShipsFormGroup {
    const shipsRawValue = {
      ...this.getFormDefaults(),
      ...ships,
    };
    return new FormGroup<ShipsFormGroupContent>({
      id: new FormControl(
        { value: shipsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      shipId: new FormControl(shipsRawValue.shipId, {
        validators: [Validators.required],
      }),
      userId: new FormControl(shipsRawValue.userId, {
        validators: [Validators.required],
      }),
      tl: new FormControl(shipsRawValue.tl, {
        validators: [Validators.required],
      }),
      tonnage: new FormControl(shipsRawValue.tonnage, {
        validators: [Validators.required],
      }),
      conf: new FormControl(shipsRawValue.conf, {
        validators: [Validators.required],
      }),
      code: new FormControl(shipsRawValue.code, {
        validators: [Validators.required],
      }),
      hull: new FormControl(shipsRawValue.hull, {
        validators: [Validators.required],
      }),
      structure: new FormControl(shipsRawValue.structure, {
        validators: [Validators.required],
      }),
      sections: new FormControl(shipsRawValue.sections, {
        validators: [Validators.required],
      }),
      capital: new FormControl(shipsRawValue.capital, {
        validators: [Validators.required],
      }),
      military: new FormControl(shipsRawValue.military, {
        validators: [Validators.required],
      }),
      user: new FormControl(shipsRawValue.user),
    });
  }

  getShips(form: ShipsFormGroup): IShips | NewShips {
    return form.getRawValue() as IShips | NewShips;
  }

  resetForm(form: ShipsFormGroup, ships: ShipsFormGroupInput): void {
    const shipsRawValue = { ...this.getFormDefaults(), ...ships };
    form.reset(
      {
        ...shipsRawValue,
        id: { value: shipsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ShipsFormDefaults {
    return {
      id: null,
      capital: false,
      military: false,
    };
  }
}
