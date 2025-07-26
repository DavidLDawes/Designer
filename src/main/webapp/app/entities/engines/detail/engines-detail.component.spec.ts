import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EnginesDetailComponent } from './engines-detail.component';

describe('Engines Management Detail Component', () => {
  let comp: EnginesDetailComponent;
  let fixture: ComponentFixture<EnginesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnginesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./engines-detail.component').then(m => m.EnginesDetailComponent),
              resolve: { engines: () => of({ id: 6601 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EnginesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnginesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load engines on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EnginesDetailComponent);

      // THEN
      expect(instance.engines()).toEqual(expect.objectContaining({ id: 6601 }));
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
