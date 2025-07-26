import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ShipsDetailComponent } from './ships-detail.component';

describe('Ships Management Detail Component', () => {
  let comp: ShipsDetailComponent;
  let fixture: ComponentFixture<ShipsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShipsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ships-detail.component').then(m => m.ShipsDetailComponent),
              resolve: { ships: () => of({ id: 14125 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ShipsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShipsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ships on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ShipsDetailComponent);

      // THEN
      expect(instance.ships()).toEqual(expect.objectContaining({ id: 14125 }));
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
