import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { DefensesService } from '../service/defenses.service';
import { IDefenses } from '../defenses.model';
import { DefensesFormService } from './defenses-form.service';

import { DefensesUpdateComponent } from './defenses-update.component';

describe('Defenses Management Update Component', () => {
  let comp: DefensesUpdateComponent;
  let fixture: ComponentFixture<DefensesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let defensesFormService: DefensesFormService;
  let defensesService: DefensesService;
  let shipsService: ShipsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DefensesUpdateComponent],
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
      .overrideTemplate(DefensesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DefensesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    defensesFormService = TestBed.inject(DefensesFormService);
    defensesService = TestBed.inject(DefensesService);
    shipsService = TestBed.inject(ShipsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ships query and add missing value', () => {
      const defenses: IDefenses = { id: 27448 };
      const ships: IShips = { id: 14125 };
      defenses.ships = ships;

      const shipsCollection: IShips[] = [{ id: 14125 }];
      jest.spyOn(shipsService, 'query').mockReturnValue(of(new HttpResponse({ body: shipsCollection })));
      const additionalShips = [ships];
      const expectedCollection: IShips[] = [...additionalShips, ...shipsCollection];
      jest.spyOn(shipsService, 'addShipsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ defenses });
      comp.ngOnInit();

      expect(shipsService.query).toHaveBeenCalled();
      expect(shipsService.addShipsToCollectionIfMissing).toHaveBeenCalledWith(
        shipsCollection,
        ...additionalShips.map(expect.objectContaining),
      );
      expect(comp.shipsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const defenses: IDefenses = { id: 27448 };
      const ships: IShips = { id: 14125 };
      defenses.ships = ships;

      activatedRoute.data = of({ defenses });
      comp.ngOnInit();

      expect(comp.shipsSharedCollection).toContainEqual(ships);
      expect(comp.defenses).toEqual(defenses);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDefenses>>();
      const defenses = { id: 7034 };
      jest.spyOn(defensesFormService, 'getDefenses').mockReturnValue(defenses);
      jest.spyOn(defensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ defenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: defenses }));
      saveSubject.complete();

      // THEN
      expect(defensesFormService.getDefenses).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(defensesService.update).toHaveBeenCalledWith(expect.objectContaining(defenses));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDefenses>>();
      const defenses = { id: 7034 };
      jest.spyOn(defensesFormService, 'getDefenses').mockReturnValue({ id: null });
      jest.spyOn(defensesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ defenses: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: defenses }));
      saveSubject.complete();

      // THEN
      expect(defensesFormService.getDefenses).toHaveBeenCalled();
      expect(defensesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDefenses>>();
      const defenses = { id: 7034 };
      jest.spyOn(defensesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ defenses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(defensesService.update).toHaveBeenCalled();
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
