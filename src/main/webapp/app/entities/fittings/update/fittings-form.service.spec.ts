import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../fittings.test-samples';

import { FittingsFormService } from './fittings-form.service';

describe('Fittings Form Service', () => {
  let service: FittingsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FittingsFormService);
  });

  describe('Service methods', () => {
    describe('createFittingsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFittingsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fitting: expect.any(Object),
            fShipId: expect.any(Object),
            count: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });

      it('passing IFittings should create a new form with FormGroup', () => {
        const formGroup = service.createFittingsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fitting: expect.any(Object),
            fShipId: expect.any(Object),
            count: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });
    });

    describe('getFittings', () => {
      it('should return NewFittings for default Fittings initial value', () => {
        const formGroup = service.createFittingsFormGroup(sampleWithNewData);

        const fittings = service.getFittings(formGroup) as any;

        expect(fittings).toMatchObject(sampleWithNewData);
      });

      it('should return NewFittings for empty Fittings initial value', () => {
        const formGroup = service.createFittingsFormGroup();

        const fittings = service.getFittings(formGroup) as any;

        expect(fittings).toMatchObject({});
      });

      it('should return IFittings', () => {
        const formGroup = service.createFittingsFormGroup(sampleWithRequiredData);

        const fittings = service.getFittings(formGroup) as any;

        expect(fittings).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFittings should not enable id FormControl', () => {
        const formGroup = service.createFittingsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFittings should disable id FormControl', () => {
        const formGroup = service.createFittingsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
