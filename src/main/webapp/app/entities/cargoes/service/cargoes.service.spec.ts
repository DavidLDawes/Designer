import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICargoes } from '../cargoes.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cargoes.test-samples';

import { CargoesService } from './cargoes.service';

const requireRestSample: ICargoes = {
  ...sampleWithRequiredData,
};

describe('Cargoes Service', () => {
  let service: CargoesService;
  let httpMock: HttpTestingController;
  let expectedResult: ICargoes | ICargoes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CargoesService);
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

    it('should create a Cargoes', () => {
      const cargoes = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cargoes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cargoes', () => {
      const cargoes = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cargoes).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cargoes', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cargoes', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cargoes', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCargoesToCollectionIfMissing', () => {
      it('should add a Cargoes to an empty array', () => {
        const cargoes: ICargoes = sampleWithRequiredData;
        expectedResult = service.addCargoesToCollectionIfMissing([], cargoes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cargoes);
      });

      it('should not add a Cargoes to an array that contains it', () => {
        const cargoes: ICargoes = sampleWithRequiredData;
        const cargoesCollection: ICargoes[] = [
          {
            ...cargoes,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCargoesToCollectionIfMissing(cargoesCollection, cargoes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cargoes to an array that doesn't contain it", () => {
        const cargoes: ICargoes = sampleWithRequiredData;
        const cargoesCollection: ICargoes[] = [sampleWithPartialData];
        expectedResult = service.addCargoesToCollectionIfMissing(cargoesCollection, cargoes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cargoes);
      });

      it('should add only unique Cargoes to an array', () => {
        const cargoesArray: ICargoes[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cargoesCollection: ICargoes[] = [sampleWithRequiredData];
        expectedResult = service.addCargoesToCollectionIfMissing(cargoesCollection, ...cargoesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cargoes: ICargoes = sampleWithRequiredData;
        const cargoes2: ICargoes = sampleWithPartialData;
        expectedResult = service.addCargoesToCollectionIfMissing([], cargoes, cargoes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cargoes);
        expect(expectedResult).toContain(cargoes2);
      });

      it('should accept null and undefined values', () => {
        const cargoes: ICargoes = sampleWithRequiredData;
        expectedResult = service.addCargoesToCollectionIfMissing([], null, cargoes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cargoes);
      });

      it('should return initial array if no Cargoes is added', () => {
        const cargoesCollection: ICargoes[] = [sampleWithRequiredData];
        expectedResult = service.addCargoesToCollectionIfMissing(cargoesCollection, undefined, null);
        expect(expectedResult).toEqual(cargoesCollection);
      });
    });

    describe('compareCargoes', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCargoes(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4157 };
        const entity2 = null;

        const compareResult1 = service.compareCargoes(entity1, entity2);
        const compareResult2 = service.compareCargoes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4157 };
        const entity2 = { id: 10622 };

        const compareResult1 = service.compareCargoes(entity1, entity2);
        const compareResult2 = service.compareCargoes(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 4157 };
        const entity2 = { id: 4157 };

        const compareResult1 = service.compareCargoes(entity1, entity2);
        const compareResult2 = service.compareCargoes(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
