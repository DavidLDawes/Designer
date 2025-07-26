import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEngines } from '../engines.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../engines.test-samples';

import { EnginesService } from './engines.service';

const requireRestSample: IEngines = {
  ...sampleWithRequiredData,
};

describe('Engines Service', () => {
  let service: EnginesService;
  let httpMock: HttpTestingController;
  let expectedResult: IEngines | IEngines[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EnginesService);
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

    it('should create a Engines', () => {
      const engines = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(engines).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Engines', () => {
      const engines = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(engines).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Engines', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Engines', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Engines', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEnginesToCollectionIfMissing', () => {
      it('should add a Engines to an empty array', () => {
        const engines: IEngines = sampleWithRequiredData;
        expectedResult = service.addEnginesToCollectionIfMissing([], engines);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(engines);
      });

      it('should not add a Engines to an array that contains it', () => {
        const engines: IEngines = sampleWithRequiredData;
        const enginesCollection: IEngines[] = [
          {
            ...engines,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEnginesToCollectionIfMissing(enginesCollection, engines);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Engines to an array that doesn't contain it", () => {
        const engines: IEngines = sampleWithRequiredData;
        const enginesCollection: IEngines[] = [sampleWithPartialData];
        expectedResult = service.addEnginesToCollectionIfMissing(enginesCollection, engines);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(engines);
      });

      it('should add only unique Engines to an array', () => {
        const enginesArray: IEngines[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const enginesCollection: IEngines[] = [sampleWithRequiredData];
        expectedResult = service.addEnginesToCollectionIfMissing(enginesCollection, ...enginesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const engines: IEngines = sampleWithRequiredData;
        const engines2: IEngines = sampleWithPartialData;
        expectedResult = service.addEnginesToCollectionIfMissing([], engines, engines2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(engines);
        expect(expectedResult).toContain(engines2);
      });

      it('should accept null and undefined values', () => {
        const engines: IEngines = sampleWithRequiredData;
        expectedResult = service.addEnginesToCollectionIfMissing([], null, engines, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(engines);
      });

      it('should return initial array if no Engines is added', () => {
        const enginesCollection: IEngines[] = [sampleWithRequiredData];
        expectedResult = service.addEnginesToCollectionIfMissing(enginesCollection, undefined, null);
        expect(expectedResult).toEqual(enginesCollection);
      });
    });

    describe('compareEngines', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEngines(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6601 };
        const entity2 = null;

        const compareResult1 = service.compareEngines(entity1, entity2);
        const compareResult2 = service.compareEngines(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6601 };
        const entity2 = { id: 2793 };

        const compareResult1 = service.compareEngines(entity1, entity2);
        const compareResult2 = service.compareEngines(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6601 };
        const entity2 = { id: 6601 };

        const compareResult1 = service.compareEngines(entity1, entity2);
        const compareResult2 = service.compareEngines(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
