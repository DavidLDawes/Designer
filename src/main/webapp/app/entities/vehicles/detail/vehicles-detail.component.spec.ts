import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VehiclesDetailComponent } from './vehicles-detail.component';

describe('Vehicles Management Detail Component', () => {
  let comp: VehiclesDetailComponent;
  let fixture: ComponentFixture<VehiclesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehiclesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vehicles-detail.component').then(m => m.VehiclesDetailComponent),
              resolve: { vehicles: () => of({ id: 14755 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VehiclesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VehiclesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vehicles on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VehiclesDetailComponent);

      // THEN
      expect(instance.vehicles()).toEqual(expect.objectContaining({ id: 14755 }));
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
