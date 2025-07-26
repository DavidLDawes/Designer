import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CargoesDetailComponent } from './cargoes-detail.component';

describe('Cargoes Management Detail Component', () => {
  let comp: CargoesDetailComponent;
  let fixture: ComponentFixture<CargoesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CargoesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cargoes-detail.component').then(m => m.CargoesDetailComponent),
              resolve: { cargoes: () => of({ id: 4157 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CargoesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CargoesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load cargoes on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CargoesDetailComponent);

      // THEN
      expect(instance.cargoes()).toEqual(expect.objectContaining({ id: 4157 }));
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
