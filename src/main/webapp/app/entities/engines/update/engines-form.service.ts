import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEngines, NewEngines } from '../engines.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEngines for edit and NewEnginesFormGroupInput for create.
 */
type EnginesFormGroupInput = IEngines | PartialWithRequiredKeyOf<NewEngines>;

type EnginesFormDefaults = Pick<NewEngines, 'id'>;

type EnginesFormGroupContent = {
  id: FormControl<IEngines['id'] | NewEngines['id']>;
  eShipId: FormControl<IEngines['eShipId']>;
  engine: FormControl<IEngines['engine']>;
  mass: FormControl<IEngines['mass']>;
  cost: FormControl<IEngines['cost']>;
  tl: FormControl<IEngines['tl']>;
  ships: FormControl<IEngines['ships']>;
};

export type EnginesFormGroup = FormGroup<EnginesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnginesFormService {
  createEnginesFormGroup(engines: EnginesFormGroupInput = { id: null }): EnginesFormGroup {
    const enginesRawValue = {
      ...this.getFormDefaults(),
      ...engines,
    };
    return new FormGroup<EnginesFormGroupContent>({
      id: new FormControl(
        { value: enginesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      eShipId: new FormControl(enginesRawValue.eShipId, {
        validators: [Validators.required],
      }),
      engine: new FormControl(enginesRawValue.engine, {
        validators: [Validators.required],
      }),
      mass: new FormControl(enginesRawValue.mass, {
        validators: [Validators.required],
      }),
      cost: new FormControl(enginesRawValue.cost, {
        validators: [Validators.required],
      }),
      tl: new FormControl(enginesRawValue.tl, {
        validators: [Validators.required],
      }),
      ships: new FormControl(enginesRawValue.ships),
    });
  }

  getEngines(form: EnginesFormGroup): IEngines | NewEngines {
    return form.getRawValue() as IEngines | NewEngines;
  }

  resetForm(form: EnginesFormGroup, engines: EnginesFormGroupInput): void {
    const enginesRawValue = { ...this.getFormDefaults(), ...engines };
    form.reset(
      {
        ...enginesRawValue,
        id: { value: enginesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnginesFormDefaults {
    return {
      id: null,
    };
  }
}
