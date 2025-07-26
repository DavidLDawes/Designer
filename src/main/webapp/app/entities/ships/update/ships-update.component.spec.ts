import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ShipsService } from '../service/ships.service';
import { IShips } from '../ships.model';
import { ShipsFormService } from './ships-form.service';

import { ShipsUpdateComponent } from './ships-update.component';

describe('Ships Management Update Component', () => {
  let comp: ShipsUpdateComponent;
  let fixture: ComponentFixture<ShipsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shipsFormService: ShipsFormService;
  let shipsService: ShipsService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ShipsUpdateComponent],
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
      .overrideTemplate(ShipsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShipsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shipsFormService = TestBed.inject(ShipsFormService);
    shipsService = TestBed.inject(ShipsService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const ships: IShips = { id: 9531 };
      const user: IUser = { id: 3944 };
      ships.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ships });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ships: IShips = { id: 9531 };
      const user: IUser = { id: 3944 };
      ships.user = user;

      activatedRoute.data = of({ ships });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.ships).toEqual(ships);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShips>>();
      const ships = { id: 14125 };
      jest.spyOn(shipsFormService, 'getShips').mockReturnValue(ships);
      jest.spyOn(shipsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ships });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ships }));
      saveSubject.complete();

      // THEN
      expect(shipsFormService.getShips).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shipsService.update).toHaveBeenCalledWith(expect.objectContaining(ships));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShips>>();
      const ships = { id: 14125 };
      jest.spyOn(shipsFormService, 'getShips').mockReturnValue({ id: null });
      jest.spyOn(shipsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ships: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ships }));
      saveSubject.complete();

      // THEN
      expect(shipsFormService.getShips).toHaveBeenCalled();
      expect(shipsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShips>>();
      const ships = { id: 14125 };
      jest.spyOn(shipsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ships });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shipsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
