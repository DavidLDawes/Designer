import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVehicles } from '../vehicles.model';

@Component({
  selector: 'jhi-vehicles-detail',
  templateUrl: './vehicles-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VehiclesDetailComponent {
  vehicles = input<IVehicles | null>(null);

  previousState(): void {
    window.history.back();
  }
}
