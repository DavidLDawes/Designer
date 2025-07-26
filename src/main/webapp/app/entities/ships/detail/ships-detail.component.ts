import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IShips } from '../ships.model';

@Component({
  selector: 'jhi-ships-detail',
  templateUrl: './ships-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ShipsDetailComponent {
  ships = input<IShips | null>(null);

  previousState(): void {
    window.history.back();
  }
}
