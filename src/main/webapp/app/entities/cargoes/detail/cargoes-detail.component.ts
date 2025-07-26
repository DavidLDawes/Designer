import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICargoes } from '../cargoes.model';

@Component({
  selector: 'jhi-cargoes-detail',
  templateUrl: './cargoes-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CargoesDetailComponent {
  cargoes = input<ICargoes | null>(null);

  previousState(): void {
    window.history.back();
  }
}
