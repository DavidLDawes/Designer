import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { VehiclesService } from '../service/vehicles.service';
import { IVehicles } from '../vehicles.model';
import { VehiclesFormService } from './vehicles-form.service';

import { VehiclesUpdateComponent } from './vehicles-update.component';

describe('Vehicles Management Update Component', () => {
  let comp: VehiclesUpdateComponent;
  let fixture: ComponentFixture<VehiclesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vehiclesFormService: VehiclesFormService;
  let vehiclesService: VehiclesService;
  let shipsService: ShipsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VehiclesUpdateComponent],
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
      .overrideTemplate(VehiclesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VehiclesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vehiclesFormService = TestBed.inject(VehiclesFormService);
    vehiclesService = TestBed.inject(VehiclesService);
    shipsService = TestBed.inject(ShipsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ships query and add missing value', () => {
      const vehicles: IVehicles = { id: 17893 };
      const ships: IShips = { id: 14125 };
      vehicles.ships = ships;

      const shipsCollection: IShips[] = [{ id: 14125 }];
      jest.spyOn(shipsService, 'query').mockReturnValue(of(new HttpResponse({ body: shipsCollection })));
      const additionalShips = [ships];
      const expectedCollection: IShips[] = [...additionalShips, ...shipsCollection];
      jest.spyOn(shipsService, 'addShipsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vehicles });
      comp.ngOnInit();

      expect(shipsService.query).toHaveBeenCalled();
      expect(shipsService.addShipsToCollectionIfMissing).toHaveBeenCalledWith(
        shipsCollection,
        ...additionalShips.map(expect.objectContaining),
      );
      expect(comp.shipsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const vehicles: IVehicles = { id: 17893 };
      const ships: IShips = { id: 14125 };
      vehicles.ships = ships;

      activatedRoute.data = of({ vehicles });
      comp.ngOnInit();

      expect(comp.shipsSharedCollection).toContainEqual(ships);
      expect(comp.vehicles).toEqual(vehicles);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicles>>();
      const vehicles = { id: 14755 };
      jest.spyOn(vehiclesFormService, 'getVehicles').mockReturnValue(vehicles);
      jest.spyOn(vehiclesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicles }));
      saveSubject.complete();

      // THEN
      expect(vehiclesFormService.getVehicles).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vehiclesService.update).toHaveBeenCalledWith(expect.objectContaining(vehicles));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicles>>();
      const vehicles = { id: 14755 };
      jest.spyOn(vehiclesFormService, 'getVehicles').mockReturnValue({ id: null });
      jest.spyOn(vehiclesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicles: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vehicles }));
      saveSubject.complete();

      // THEN
      expect(vehiclesFormService.getVehicles).toHaveBeenCalled();
      expect(vehiclesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVehicles>>();
      const vehicles = { id: 14755 };
      jest.spyOn(vehiclesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vehicles });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vehiclesService.update).toHaveBeenCalled();
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
