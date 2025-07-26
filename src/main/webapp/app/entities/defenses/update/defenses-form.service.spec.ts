import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../defenses.test-samples';

import { DefensesFormService } from './defenses-form.service';

describe('Defenses Form Service', () => {
  let service: DefensesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefensesFormService);
  });

  describe('Service methods', () => {
    describe('createDefensesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDefensesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            defense: expect.any(Object),
            dShipId: expect.any(Object),
            battery: expect.any(Object),
            count: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });

      it('passing IDefenses should create a new form with FormGroup', () => {
        const formGroup = service.createDefensesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            defense: expect.any(Object),
            dShipId: expect.any(Object),
            battery: expect.any(Object),
            count: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });
    });

    describe('getDefenses', () => {
      it('should return NewDefenses for default Defenses initial value', () => {
        const formGroup = service.createDefensesFormGroup(sampleWithNewData);

        const defenses = service.getDefenses(formGroup) as any;

        expect(defenses).toMatchObject(sampleWithNewData);
      });

      it('should return NewDefenses for empty Defenses initial value', () => {
        const formGroup = service.createDefensesFormGroup();

        const defenses = service.getDefenses(formGroup) as any;

        expect(defenses).toMatchObject({});
      });

      it('should return IDefenses', () => {
        const formGroup = service.createDefensesFormGroup(sampleWithRequiredData);

        const defenses = service.getDefenses(formGroup) as any;

        expect(defenses).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDefenses should not enable id FormControl', () => {
        const formGroup = service.createDefensesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDefenses should disable id FormControl', () => {
        const formGroup = service.createDefensesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
