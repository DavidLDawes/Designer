import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ships.test-samples';

import { ShipsFormService } from './ships-form.service';

describe('Ships Form Service', () => {
  let service: ShipsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShipsFormService);
  });

  describe('Service methods', () => {
    describe('createShipsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShipsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            shipId: expect.any(Object),
            userId: expect.any(Object),
            tl: expect.any(Object),
            tonnage: expect.any(Object),
            conf: expect.any(Object),
            code: expect.any(Object),
            hull: expect.any(Object),
            structure: expect.any(Object),
            sections: expect.any(Object),
            capital: expect.any(Object),
            military: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IShips should create a new form with FormGroup', () => {
        const formGroup = service.createShipsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            shipId: expect.any(Object),
            userId: expect.any(Object),
            tl: expect.any(Object),
            tonnage: expect.any(Object),
            conf: expect.any(Object),
            code: expect.any(Object),
            hull: expect.any(Object),
            structure: expect.any(Object),
            sections: expect.any(Object),
            capital: expect.any(Object),
            military: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getShips', () => {
      it('should return NewShips for default Ships initial value', () => {
        const formGroup = service.createShipsFormGroup(sampleWithNewData);

        const ships = service.getShips(formGroup) as any;

        expect(ships).toMatchObject(sampleWithNewData);
      });

      it('should return NewShips for empty Ships initial value', () => {
        const formGroup = service.createShipsFormGroup();

        const ships = service.getShips(formGroup) as any;

        expect(ships).toMatchObject({});
      });

      it('should return IShips', () => {
        const formGroup = service.createShipsFormGroup(sampleWithRequiredData);

        const ships = service.getShips(formGroup) as any;

        expect(ships).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShips should not enable id FormControl', () => {
        const formGroup = service.createShipsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShips should disable id FormControl', () => {
        const formGroup = service.createShipsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
