import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IShips } from '../ships.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ships.test-samples';

import { ShipsService } from './ships.service';

const requireRestSample: IShips = {
  ...sampleWithRequiredData,
};

describe('Ships Service', () => {
  let service: ShipsService;
  let httpMock: HttpTestingController;
  let expectedResult: IShips | IShips[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ShipsService);
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

    it('should create a Ships', () => {
      const ships = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ships).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ships', () => {
      const ships = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ships).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ships', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ships', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ships', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addShipsToCollectionIfMissing', () => {
      it('should add a Ships to an empty array', () => {
        const ships: IShips = sampleWithRequiredData;
        expectedResult = service.addShipsToCollectionIfMissing([], ships);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ships);
      });

      it('should not add a Ships to an array that contains it', () => {
        const ships: IShips = sampleWithRequiredData;
        const shipsCollection: IShips[] = [
          {
            ...ships,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShipsToCollectionIfMissing(shipsCollection, ships);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ships to an array that doesn't contain it", () => {
        const ships: IShips = sampleWithRequiredData;
        const shipsCollection: IShips[] = [sampleWithPartialData];
        expectedResult = service.addShipsToCollectionIfMissing(shipsCollection, ships);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ships);
      });

      it('should add only unique Ships to an array', () => {
        const shipsArray: IShips[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shipsCollection: IShips[] = [sampleWithRequiredData];
        expectedResult = service.addShipsToCollectionIfMissing(shipsCollection, ...shipsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ships: IShips = sampleWithRequiredData;
        const ships2: IShips = sampleWithPartialData;
        expectedResult = service.addShipsToCollectionIfMissing([], ships, ships2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ships);
        expect(expectedResult).toContain(ships2);
      });

      it('should accept null and undefined values', () => {
        const ships: IShips = sampleWithRequiredData;
        expectedResult = service.addShipsToCollectionIfMissing([], null, ships, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ships);
      });

      it('should return initial array if no Ships is added', () => {
        const shipsCollection: IShips[] = [sampleWithRequiredData];
        expectedResult = service.addShipsToCollectionIfMissing(shipsCollection, undefined, null);
        expect(expectedResult).toEqual(shipsCollection);
      });
    });

    describe('compareShips', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShips(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14125 };
        const entity2 = null;

        const compareResult1 = service.compareShips(entity1, entity2);
        const compareResult2 = service.compareShips(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14125 };
        const entity2 = { id: 9531 };

        const compareResult1 = service.compareShips(entity1, entity2);
        const compareResult2 = service.compareShips(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14125 };
        const entity2 = { id: 14125 };

        const compareResult1 = service.compareShips(entity1, entity2);
        const compareResult2 = service.compareShips(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
