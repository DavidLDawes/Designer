import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWeapons } from '../weapons.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../weapons.test-samples';

import { WeaponsService } from './weapons.service';

const requireRestSample: IWeapons = {
  ...sampleWithRequiredData,
};

describe('Weapons Service', () => {
  let service: WeaponsService;
  let httpMock: HttpTestingController;
  let expectedResult: IWeapons | IWeapons[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WeaponsService);
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

    it('should create a Weapons', () => {
      const weapons = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(weapons).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Weapons', () => {
      const weapons = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(weapons).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Weapons', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Weapons', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Weapons', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWeaponsToCollectionIfMissing', () => {
      it('should add a Weapons to an empty array', () => {
        const weapons: IWeapons = sampleWithRequiredData;
        expectedResult = service.addWeaponsToCollectionIfMissing([], weapons);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weapons);
      });

      it('should not add a Weapons to an array that contains it', () => {
        const weapons: IWeapons = sampleWithRequiredData;
        const weaponsCollection: IWeapons[] = [
          {
            ...weapons,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWeaponsToCollectionIfMissing(weaponsCollection, weapons);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Weapons to an array that doesn't contain it", () => {
        const weapons: IWeapons = sampleWithRequiredData;
        const weaponsCollection: IWeapons[] = [sampleWithPartialData];
        expectedResult = service.addWeaponsToCollectionIfMissing(weaponsCollection, weapons);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weapons);
      });

      it('should add only unique Weapons to an array', () => {
        const weaponsArray: IWeapons[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const weaponsCollection: IWeapons[] = [sampleWithRequiredData];
        expectedResult = service.addWeaponsToCollectionIfMissing(weaponsCollection, ...weaponsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const weapons: IWeapons = sampleWithRequiredData;
        const weapons2: IWeapons = sampleWithPartialData;
        expectedResult = service.addWeaponsToCollectionIfMissing([], weapons, weapons2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weapons);
        expect(expectedResult).toContain(weapons2);
      });

      it('should accept null and undefined values', () => {
        const weapons: IWeapons = sampleWithRequiredData;
        expectedResult = service.addWeaponsToCollectionIfMissing([], null, weapons, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weapons);
      });

      it('should return initial array if no Weapons is added', () => {
        const weaponsCollection: IWeapons[] = [sampleWithRequiredData];
        expectedResult = service.addWeaponsToCollectionIfMissing(weaponsCollection, undefined, null);
        expect(expectedResult).toEqual(weaponsCollection);
      });
    });

    describe('compareWeapons', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWeapons(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4978 };
        const entity2 = null;

        const compareResult1 = service.compareWeapons(entity1, entity2);
        const compareResult2 = service.compareWeapons(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4978 };
        const entity2 = { id: 14189 };

        const compareResult1 = service.compareWeapons(entity1, entity2);
        const compareResult2 = service.compareWeapons(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4978 };
        const entity2 = { id: 4978 };

        const compareResult1 = service.compareWeapons(entity1, entity2);
        const compareResult2 = service.compareWeapons(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
