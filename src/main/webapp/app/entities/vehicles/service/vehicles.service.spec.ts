import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVehicles } from '../vehicles.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vehicles.test-samples';

import { VehiclesService } from './vehicles.service';

const requireRestSample: IVehicles = {
  ...sampleWithRequiredData,
};

describe('Vehicles Service', () => {
  let service: VehiclesService;
  let httpMock: HttpTestingController;
  let expectedResult: IVehicles | IVehicles[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VehiclesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Vehicles', () => {
      const vehicles = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vehicles).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vehicles', () => {
      const vehicles = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vehicles).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vehicles', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vehicles', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vehicles', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVehiclesToCollectionIfMissing', () => {
      it('should add a Vehicles to an empty array', () => {
        const vehicles: IVehicles = sampleWithRequiredData;
        expectedResult = service.addVehiclesToCollectionIfMissing([], vehicles);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicles);
      });

      it('should not add a Vehicles to an array that contains it', () => {
        const vehicles: IVehicles = sampleWithRequiredData;
        const vehiclesCollection: IVehicles[] = [
          {
            ...vehicles,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVehiclesToCollectionIfMissing(vehiclesCollection, vehicles);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vehicles to an array that doesn't contain it", () => {
        const vehicles: IVehicles = sampleWithRequiredData;
        const vehiclesCollection: IVehicles[] = [sampleWithPartialData];
        expectedResult = service.addVehiclesToCollectionIfMissing(vehiclesCollection, vehicles);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicles);
      });

      it('should add only unique Vehicles to an array', () => {
        const vehiclesArray: IVehicles[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vehiclesCollection: IVehicles[] = [sampleWithRequiredData];
        expectedResult = service.addVehiclesToCollectionIfMissing(vehiclesCollection, ...vehiclesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicles: IVehicles = sampleWithRequiredData;
        const vehicles2: IVehicles = sampleWithPartialData;
        expectedResult = service.addVehiclesToCollectionIfMissing([], vehicles, vehicles2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicles);
        expect(expectedResult).toContain(vehicles2);
      });

      it('should accept null and undefined values', () => {
        const vehicles: IVehicles = sampleWithRequiredData;
        expectedResult = service.addVehiclesToCollectionIfMissing([], null, vehicles, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicles);
      });

      it('should return initial array if no Vehicles is added', () => {
        const vehiclesCollection: IVehicles[] = [sampleWithRequiredData];
        expectedResult = service.addVehiclesToCollectionIfMissing(vehiclesCollection, undefined, null);
        expect(expectedResult).toEqual(vehiclesCollection);
      });
    });

    describe('compareVehicles', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVehicles(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14755 };
        const entity2 = null;

        const compareResult1 = service.compareVehicles(entity1, entity2);
        const compareResult2 = service.compareVehicles(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14755 };
        const entity2 = { id: 17893 };

        const compareResult1 = service.compareVehicles(entity1, entity2);
        const compareResult2 = service.compareVehicles(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14755 };
        const entity2 = { id: 14755 };

        const compareResult1 = service.compareVehicles(entity1, entity2);
        const compareResult2 = service.compareVehicles(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
