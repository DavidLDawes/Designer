import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFittings } from '../fittings.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../fittings.test-samples';

import { FittingsService } from './fittings.service';

const requireRestSample: IFittings = {
  ...sampleWithRequiredData,
};

describe('Fittings Service', () => {
  let service: FittingsService;
  let httpMock: HttpTestingController;
  let expectedResult: IFittings | IFittings[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FittingsService);
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

    it('should create a Fittings', () => {
      const fittings = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fittings).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Fittings', () => {
      const fittings = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fittings).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Fittings', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Fittings', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Fittings', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFittingsToCollectionIfMissing', () => {
      it('should add a Fittings to an empty array', () => {
        const fittings: IFittings = sampleWithRequiredData;
        expectedResult = service.addFittingsToCollectionIfMissing([], fittings);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fittings);
      });

      it('should not add a Fittings to an array that contains it', () => {
        const fittings: IFittings = sampleWithRequiredData;
        const fittingsCollection: IFittings[] = [
          {
            ...fittings,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFittingsToCollectionIfMissing(fittingsCollection, fittings);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Fittings to an array that doesn't contain it", () => {
        const fittings: IFittings = sampleWithRequiredData;
        const fittingsCollection: IFittings[] = [sampleWithPartialData];
        expectedResult = service.addFittingsToCollectionIfMissing(fittingsCollection, fittings);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fittings);
      });

      it('should add only unique Fittings to an array', () => {
        const fittingsArray: IFittings[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fittingsCollection: IFittings[] = [sampleWithRequiredData];
        expectedResult = service.addFittingsToCollectionIfMissing(fittingsCollection, ...fittingsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fittings: IFittings = sampleWithRequiredData;
        const fittings2: IFittings = sampleWithPartialData;
        expectedResult = service.addFittingsToCollectionIfMissing([], fittings, fittings2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fittings);
        expect(expectedResult).toContain(fittings2);
      });

      it('should accept null and undefined values', () => {
        const fittings: IFittings = sampleWithRequiredData;
        expectedResult = service.addFittingsToCollectionIfMissing([], null, fittings, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fittings);
      });

      it('should return initial array if no Fittings is added', () => {
        const fittingsCollection: IFittings[] = [sampleWithRequiredData];
        expectedResult = service.addFittingsToCollectionIfMissing(fittingsCollection, undefined, null);
        expect(expectedResult).toEqual(fittingsCollection);
      });
    });

    describe('compareFittings', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFittings(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27188 };
        const entity2 = null;

        const compareResult1 = service.compareFittings(entity1, entity2);
        const compareResult2 = service.compareFittings(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27188 };
        const entity2 = { id: 7961 };

        const compareResult1 = service.compareFittings(entity1, entity2);
        const compareResult2 = service.compareFittings(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 27188 };
        const entity2 = { id: 27188 };

        const compareResult1 = service.compareFittings(entity1, entity2);
        const compareResult2 = service.compareFittings(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
