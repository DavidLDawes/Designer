import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { WeaponsDetailComponent } from './weapons-detail.component';

describe('Weapons Management Detail Component', () => {
  let comp: WeaponsDetailComponent;
  let fixture: ComponentFixture<WeaponsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WeaponsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./weapons-detail.component').then(m => m.WeaponsDetailComponent),
              resolve: { weapons: () => of({ id: 4978 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(WeaponsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WeaponsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load weapons on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WeaponsDetailComponent);

      // THEN
      expect(instance.weapons()).toEqual(expect.objectContaining({ id: 4978 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
