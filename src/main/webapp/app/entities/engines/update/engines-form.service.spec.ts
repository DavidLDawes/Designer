import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../engines.test-samples';

import { EnginesFormService } from './engines-form.service';

describe('Engines Form Service', () => {
  let service: EnginesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnginesFormService);
  });

  describe('Service methods', () => {
    describe('createEnginesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEnginesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eShipId: expect.any(Object),
            engine: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            tl: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });

      it('passing IEngines should create a new form with FormGroup', () => {
        const formGroup = service.createEnginesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eShipId: expect.any(Object),
            engine: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            tl: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });
    });

    describe('getEngines', () => {
      it('should return NewEngines for default Engines initial value', () => {
        const formGroup = service.createEnginesFormGroup(sampleWithNewData);

        const engines = service.getEngines(formGroup) as any;

        expect(engines).toMatchObject(sampleWithNewData);
      });

      it('should return NewEngines for empty Engines initial value', () => {
        const formGroup = service.createEnginesFormGroup();

        const engines = service.getEngines(formGroup) as any;

        expect(engines).toMatchObject({});
      });

      it('should return IEngines', () => {
        const formGroup = service.createEnginesFormGroup(sampleWithRequiredData);

        const engines = service.getEngines(formGroup) as any;

        expect(engines).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEngines should not enable id FormControl', () => {
        const formGroup = service.createEnginesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEngines should disable id FormControl', () => {
        const formGroup = service.createEnginesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
