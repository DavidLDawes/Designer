import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { EnginesService } from '../service/engines.service';
import { IEngines } from '../engines.model';
import { EnginesFormService } from './engines-form.service';

import { EnginesUpdateComponent } from './engines-update.component';

describe('Engines Management Update Component', () => {
  let comp: EnginesUpdateComponent;
  let fixture: ComponentFixture<EnginesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enginesFormService: EnginesFormService;
  let enginesService: EnginesService;
  let shipsService: ShipsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EnginesUpdateComponent],
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
      .overrideTemplate(EnginesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnginesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enginesFormService = TestBed.inject(EnginesFormService);
    enginesService = TestBed.inject(EnginesService);
    shipsService = TestBed.inject(ShipsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ships query and add missing value', () => {
      const engines: IEngines = { id: 2793 };
      const ships: IShips = { id: 14125 };
      engines.ships = ships;

      const shipsCollection: IShips[] = [{ id: 14125 }];
      jest.spyOn(shipsService, 'query').mockReturnValue(of(new HttpResponse({ body: shipsCollection })));
      const additionalShips = [ships];
      const expectedCollection: IShips[] = [...additionalShips, ...shipsCollection];
      jest.spyOn(shipsService, 'addShipsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ engines });
      comp.ngOnInit();

      expect(shipsService.query).toHaveBeenCalled();
      expect(shipsService.addShipsToCollectionIfMissing).toHaveBeenCalledWith(
        shipsCollection,
        ...additionalShips.map(expect.objectContaining),
      );
      expect(comp.shipsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const engines: IEngines = { id: 2793 };
      const ships: IShips = { id: 14125 };
      engines.ships = ships;

      activatedRoute.data = of({ engines });
      comp.ngOnInit();

      expect(comp.shipsSharedCollection).toContainEqual(ships);
      expect(comp.engines).toEqual(engines);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEngines>>();
      const engines = { id: 6601 };
      jest.spyOn(enginesFormService, 'getEngines').mockReturnValue(engines);
      jest.spyOn(enginesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ engines });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: engines }));
      saveSubject.complete();

      // THEN
      expect(enginesFormService.getEngines).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enginesService.update).toHaveBeenCalledWith(expect.objectContaining(engines));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEngines>>();
      const engines = { id: 6601 };
      jest.spyOn(enginesFormService, 'getEngines').mockReturnValue({ id: null });
      jest.spyOn(enginesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ engines: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: engines }));
      saveSubject.complete();

      // THEN
      expect(enginesFormService.getEngines).toHaveBeenCalled();
      expect(enginesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEngines>>();
      const engines = { id: 6601 };
      jest.spyOn(enginesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ engines });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enginesService.update).toHaveBeenCalled();
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
