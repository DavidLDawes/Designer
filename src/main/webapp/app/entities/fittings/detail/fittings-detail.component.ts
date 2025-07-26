import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFittings } from '../fittings.model';

@Component({
  selector: 'jhi-fittings-detail',
  templateUrl: './fittings-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FittingsDetailComponent {
  fittings = input<IFittings | null>(null);

  previousState(): void {
    window.history.back();
  }
}
