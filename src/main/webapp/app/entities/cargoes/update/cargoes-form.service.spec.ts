import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cargoes.test-samples';

import { CargoesFormService } from './cargoes-form.service';

describe('Cargoes Form Service', () => {
  let service: CargoesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CargoesFormService);
  });

  describe('Service methods', () => {
    describe('createCargoesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCargoesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cargo: expect.any(Object),
            cShipId: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });

      it('passing ICargoes should create a new form with FormGroup', () => {
        const formGroup = service.createCargoesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cargo: expect.any(Object),
            cShipId: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });
    });

    describe('getCargoes', () => {
      it('should return NewCargoes for default Cargoes initial value', () => {
        const formGroup = service.createCargoesFormGroup(sampleWithNewData);

        const cargoes = service.getCargoes(formGroup) as any;

        expect(cargoes).toMatchObject(sampleWithNewData);
      });

      it('should return NewCargoes for empty Cargoes initial value', () => {
        const formGroup = service.createCargoesFormGroup();

        const cargoes = service.getCargoes(formGroup) as any;

        expect(cargoes).toMatchObject({});
      });

      it('should return ICargoes', () => {
        const formGroup = service.createCargoesFormGroup(sampleWithRequiredData);

        const cargoes = service.getCargoes(formGroup) as any;

        expect(cargoes).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICargoes should not enable id FormControl', () => {
        const formGroup = service.createCargoesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCargoes should disable id FormControl', () => {
        const formGroup = service.createCargoesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
