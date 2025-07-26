import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { CargoesService } from '../service/cargoes.service';
import { ICargoes } from '../cargoes.model';
import { CargoesFormService } from './cargoes-form.service';

import { CargoesUpdateComponent } from './cargoes-update.component';

describe('Cargoes Management Update Component', () => {
  let comp: CargoesUpdateComponent;
  let fixture: ComponentFixture<CargoesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cargoesFormService: CargoesFormService;
  let cargoesService: CargoesService;
  let shipsService: ShipsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CargoesUpdateComponent],
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
      .overrideTemplate(CargoesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CargoesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cargoesFormService = TestBed.inject(CargoesFormService);
    cargoesService = TestBed.inject(CargoesService);
    shipsService = TestBed.inject(ShipsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ships query and add missing value', () => {
      const cargoes: ICargoes = { id: 10622 };
      const ships: IShips = { id: 14125 };
      cargoes.ships = ships;

      const shipsCollection: IShips[] = [{ id: 14125 }];
      jest.spyOn(shipsService, 'query').mockReturnValue(of(new HttpResponse({ body: shipsCollection })));
      const additionalShips = [ships];
      const expectedCollection: IShips[] = [...additionalShips, ...shipsCollection];
      jest.spyOn(shipsService, 'addShipsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cargoes });
      comp.ngOnInit();

      expect(shipsService.query).toHaveBeenCalled();
      expect(shipsService.addShipsToCollectionIfMissing).toHaveBeenCalledWith(
        shipsCollection,
        ...additionalShips.map(expect.objectContaining),
      );
      expect(comp.shipsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cargoes: ICargoes = { id: 10622 };
      const ships: IShips = { id: 14125 };
      cargoes.ships = ships;

      activatedRoute.data = of({ cargoes });
      comp.ngOnInit();

      expect(comp.shipsSharedCollection).toContainEqual(ships);
      expect(comp.cargoes).toEqual(cargoes);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICargoes>>();
      const cargoes = { id: 4157 };
      jest.spyOn(cargoesFormService, 'getCargoes').mockReturnValue(cargoes);
      jest.spyOn(cargoesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cargoes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cargoes }));
      saveSubject.complete();

      // THEN
      expect(cargoesFormService.getCargoes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cargoesService.update).toHaveBeenCalledWith(expect.objectContaining(cargoes));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICargoes>>();
      const cargoes = { id: 4157 };
      jest.spyOn(cargoesFormService, 'getCargoes').mockReturnValue({ id: null });
      jest.spyOn(cargoesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cargoes: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cargoes }));
      saveSubject.complete();

      // THEN
      expect(cargoesFormService.getCargoes).toHaveBeenCalled();
      expect(cargoesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICargoes>>();
      const cargoes = { id: 4157 };
      jest.spyOn(cargoesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cargoes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cargoesService.update).toHaveBeenCalled();
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
