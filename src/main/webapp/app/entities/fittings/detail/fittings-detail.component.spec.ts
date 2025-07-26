import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FittingsDetailComponent } from './fittings-detail.component';

describe('Fittings Management Detail Component', () => {
  let comp: FittingsDetailComponent;
  let fixture: ComponentFixture<FittingsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FittingsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fittings-detail.component').then(m => m.FittingsDetailComponent),
              resolve: { fittings: () => of({ id: 27188 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FittingsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FittingsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load fittings on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FittingsDetailComponent);

      // THEN
      expect(instance.fittings()).toEqual(expect.objectContaining({ id: 27188 }));
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
