import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { FittingsService } from '../service/fittings.service';
import { IFittings } from '../fittings.model';
import { FittingsFormService } from './fittings-form.service';

import { FittingsUpdateComponent } from './fittings-update.component';

describe('Fittings Management Update Component', () => {
  let comp: FittingsUpdateComponent;
  let fixture: ComponentFixture<FittingsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fittingsFormService: FittingsFormService;
  let fittingsService: FittingsService;
  let shipsService: ShipsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FittingsUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FittingsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FittingsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fittingsFormService = TestBed.inject(FittingsFormService);
    fittingsService = TestBed.inject(FittingsService);
    shipsService = TestBed.inject(ShipsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ships query and add missing value', () => {
      const fittings: IFittings = { id: 7961 };
      const ships: IShips = { id: 14125 };
      fittings.ships = ships;

      const shipsCollection: IShips[] = [{ id: 14125 }];
      jest.spyOn(shipsService, 'query').mockReturnValue(of(new HttpResponse({ body: shipsCollection })));
      const additionalShips = [ships];
      const expectedCollection: IShips[] = [...additionalShips, ...shipsCollection];
      jest.spyOn(shipsService, 'addShipsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fittings });
      comp.ngOnInit();

      expect(shipsService.query).toHaveBeenCalled();
      expect(shipsService.addShipsToCollectionIfMissing).toHaveBeenCalledWith(
        shipsCollection,
        ...additionalShips.map(expect.objectContaining),
      );
      expect(comp.shipsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const fittings: IFittings = { id: 7961 };
      const ships: IShips = { id: 14125 };
      fittings.ships = ships;

      activatedRoute.data = of({ fittings });
      comp.ngOnInit();

      expect(comp.shipsSharedCollection).toContainEqual(ships);
      expect(comp.fittings).toEqual(fittings);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFittings>>();
      const fittings = { id: 27188 };
      jest.spyOn(fittingsFormService, 'getFittings').mockReturnValue(fittings);
      jest.spyOn(fittingsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fittings });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fittings }));
      saveSubject.complete();

      // THEN
      expect(fittingsFormService.getFittings).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fittingsService.update).toHaveBeenCalledWith(expect.objectContaining(fittings));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFittings>>();
      const fittings = { id: 27188 };
      jest.spyOn(fittingsFormService, 'getFittings').mockReturnValue({ id: null });
      jest.spyOn(fittingsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fittings: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fittings }));
      saveSubject.complete();

      // THEN
      expect(fittingsFormService.getFittings).toHaveBeenCalled();
      expect(fittingsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFittings>>();
      const fittings = { id: 27188 };
      jest.spyOn(fittingsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fittings });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fittingsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareShips', () => {
      it('should forward to shipsService', () => {
        const entity = { id: 14125 };
        const entity2 = { id: 9531 };
        jest.spyOn(shipsService, 'compareShips');
        comp.compareShips(entity, entity2);
        expect(shipsService.compareShips).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
