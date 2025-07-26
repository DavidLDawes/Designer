import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IShips } from 'app/entities/ships/ships.model';
import { ShipsService } from 'app/entities/ships/service/ships.service';
import { WeaponsService } from '../service/weapons.service';
import { IWeapons } from '../weapons.model';
import { WeaponsFormService } from './weapons-form.service';

import { WeaponsUpdateComponent } from './weapons-update.component';

describe('Weapons Management Update Component', () => {
  let comp: WeaponsUpdateComponent;
  let fixture: ComponentFixture<WeaponsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let weaponsFormService: WeaponsFormService;
  let weaponsService: WeaponsService;
  let shipsService: ShipsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WeaponsUpdateComponent],
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
      .overrideTemplate(WeaponsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeaponsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    weaponsFormService = TestBed.inject(WeaponsFormService);
    weaponsService = TestBed.inject(WeaponsService);
    shipsService = TestBed.inject(ShipsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ships query and add missing value', () => {
      const weapons: IWeapons = { id: 14189 };
      const ships: IShips = { id: 14125 };
      weapons.ships = ships;

      const shipsCollection: IShips[] = [{ id: 14125 }];
      jest.spyOn(shipsService, 'query').mockReturnValue(of(new HttpResponse({ body: shipsCollection })));
      const additionalShips = [ships];
      const expectedCollection: IShips[] = [...additionalShips, ...shipsCollection];
      jest.spyOn(shipsService, 'addShipsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weapons });
      comp.ngOnInit();

      expect(shipsService.query).toHaveBeenCalled();
      expect(shipsService.addShipsToCollectionIfMissing).toHaveBeenCalledWith(
        shipsCollection,
        ...additionalShips.map(expect.objectContaining),
      );
      expect(comp.shipsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const weapons: IWeapons = { id: 14189 };
      const ships: IShips = { id: 14125 };
      weapons.ships = ships;

      activatedRoute.data = of({ weapons });
      comp.ngOnInit();

      expect(comp.shipsSharedCollection).toContainEqual(ships);
      expect(comp.weapons).toEqual(weapons);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWeapons>>();
      const weapons = { id: 4978 };
      jest.spyOn(weaponsFormService, 'getWeapons').mockReturnValue(weapons);
      jest.spyOn(weaponsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapons });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weapons }));
      saveSubject.complete();

      // THEN
      expect(weaponsFormService.getWeapons).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(weaponsService.update).toHaveBeenCalledWith(expect.objectContaining(weapons));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWeapons>>();
      const weapons = { id: 4978 };
      jest.spyOn(weaponsFormService, 'getWeapons').mockReturnValue({ id: null });
      jest.spyOn(weaponsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapons: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weapons }));
      saveSubject.complete();

      // THEN
      expect(weaponsFormService.getWeapons).toHaveBeenCalled();
      expect(weaponsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWeapons>>();
      const weapons = { id: 4978 };
      jest.spyOn(weaponsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapons });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(weaponsService.update).toHaveBeenCalled();
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
