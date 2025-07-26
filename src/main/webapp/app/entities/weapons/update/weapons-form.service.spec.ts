import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../weapons.test-samples';

import { WeaponsFormService } from './weapons-form.service';

describe('Weapons Form Service', () => {
  let service: WeaponsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WeaponsFormService);
  });

  describe('Service methods', () => {
    describe('createWeaponsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWeaponsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            weapon: expect.any(Object),
            wShipId: expect.any(Object),
            battery: expect.any(Object),
            count: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });

      it('passing IWeapons should create a new form with FormGroup', () => {
        const formGroup = service.createWeaponsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            weapon: expect.any(Object),
            wShipId: expect.any(Object),
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

    describe('getWeapons', () => {
      it('should return NewWeapons for default Weapons initial value', () => {
        const formGroup = service.createWeaponsFormGroup(sampleWithNewData);

        const weapons = service.getWeapons(formGroup) as any;

        expect(weapons).toMatchObject(sampleWithNewData);
      });

      it('should return NewWeapons for empty Weapons initial value', () => {
        const formGroup = service.createWeaponsFormGroup();

        const weapons = service.getWeapons(formGroup) as any;

        expect(weapons).toMatchObject({});
      });

      it('should return IWeapons', () => {
        const formGroup = service.createWeaponsFormGroup(sampleWithRequiredData);

        const weapons = service.getWeapons(formGroup) as any;

        expect(weapons).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWeapons should not enable id FormControl', () => {
        const formGroup = service.createWeaponsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWeapons should disable id FormControl', () => {
        const formGroup = service.createWeaponsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
