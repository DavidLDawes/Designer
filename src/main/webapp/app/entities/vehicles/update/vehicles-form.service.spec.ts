import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../vehicles.test-samples';

import { VehiclesFormService } from './vehicles-form.service';

describe('Vehicles Form Service', () => {
  let service: VehiclesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehiclesFormService);
  });

  describe('Service methods', () => {
    describe('createVehiclesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehiclesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicle: expect.any(Object),
            vShipId: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            repairBay: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });

      it('passing IVehicles should create a new form with FormGroup', () => {
        const formGroup = service.createVehiclesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            vehicle: expect.any(Object),
            vShipId: expect.any(Object),
            mass: expect.any(Object),
            cost: expect.any(Object),
            armored: expect.any(Object),
            repairBay: expect.any(Object),
            ships: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicles', () => {
      it('should return NewVehicles for default Vehicles initial value', () => {
        const formGroup = service.createVehiclesFormGroup(sampleWithNewData);

        const vehicles = service.getVehicles(formGroup) as any;

        expect(vehicles).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicles for empty Vehicles initial value', () => {
        const formGroup = service.createVehiclesFormGroup();

        const vehicles = service.getVehicles(formGroup) as any;

        expect(vehicles).toMatchObject({});
      });

      it('should return IVehicles', () => {
        const formGroup = service.createVehiclesFormGroup(sampleWithRequiredData);

        const vehicles = service.getVehicles(formGroup) as any;

        expect(vehicles).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicles should not enable id FormControl', () => {
        const formGroup = service.createVehiclesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicles should disable id FormControl', () => {
        const formGroup = service.createVehiclesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
