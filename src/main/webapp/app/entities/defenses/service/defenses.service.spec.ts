import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDefenses } from '../defenses.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../defenses.test-samples';

import { DefensesService } from './defenses.service';

const requireRestSample: IDefenses = {
  ...sampleWithRequiredData,
};

describe('Defenses Service', () => {
  let service: DefensesService;
  let httpMock: HttpTestingController;
  let expectedResult: IDefenses | IDefenses[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DefensesService);
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

    it('should create a Defenses', () => {
      const defenses = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(defenses).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Defenses', () => {
      const defenses = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(defenses).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Defenses', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Defenses', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Defenses', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDefensesToCollectionIfMissing', () => {
      it('should add a Defenses to an empty array', () => {
        const defenses: IDefenses = sampleWithRequiredData;
        expectedResult = service.addDefensesToCollectionIfMissing([], defenses);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(defenses);
      });

      it('should not add a Defenses to an array that contains it', () => {
        const defenses: IDefenses = sampleWithRequiredData;
        const defensesCollection: IDefenses[] = [
          {
            ...defenses,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDefensesToCollectionIfMissing(defensesCollection, defenses);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Defenses to an array that doesn't contain it", () => {
        const defenses: IDefenses = sampleWithRequiredData;
        const defensesCollection: IDefenses[] = [sampleWithPartialData];
        expectedResult = service.addDefensesToCollectionIfMissing(defensesCollection, defenses);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(defenses);
      });

      it('should add only unique Defenses to an array', () => {
        const defensesArray: IDefenses[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const defensesCollection: IDefenses[] = [sampleWithRequiredData];
        expectedResult = service.addDefensesToCollectionIfMissing(defensesCollection, ...defensesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const defenses: IDefenses = sampleWithRequiredData;
        const defenses2: IDefenses = sampleWithPartialData;
        expectedResult = service.addDefensesToCollectionIfMissing([], defenses, defenses2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(defenses);
        expect(expectedResult).toContain(defenses2);
      });

      it('should accept null and undefined values', () => {
        const defenses: IDefenses = sampleWithRequiredData;
        expectedResult = service.addDefensesToCollectionIfMissing([], null, defenses, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(defenses);
      });

      it('should return initial array if no Defenses is added', () => {
        const defensesCollection: IDefenses[] = [sampleWithRequiredData];
        expectedResult = service.addDefensesToCollectionIfMissing(defensesCollection, undefined, null);
        expect(expectedResult).toEqual(defensesCollection);
      });
    });

    describe('compareDefenses', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDefenses(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7034 };
        const entity2 = null;

        const compareResult1 = service.compareDefenses(entity1, entity2);
        const compareResult2 = service.compareDefenses(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7034 };
        const entity2 = { id: 27448 };

        const compareResult1 = service.compareDefenses(entity1, entity2);
        const compareResult2 = service.compareDefenses(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7034 };
        const entity2 = { id: 7034 };

        const compareResult1 = service.compareDefenses(entity1, entity2);
        const compareResult2 = service.compareDefenses(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
