import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DefensesDetailComponent } from './defenses-detail.component';

describe('Defenses Management Detail Component', () => {
  let comp: DefensesDetailComponent;
  let fixture: ComponentFixture<DefensesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DefensesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./defenses-detail.component').then(m => m.DefensesDetailComponent),
              resolve: { defenses: () => of({ id: 7034 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DefensesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DefensesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load defenses on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DefensesDetailComponent);

      // THEN
      expect(instance.defenses()).toEqual(expect.objectContaining({ id: 7034 }));
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
