import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IWeapons } from '../weapons.model';

@Component({
  selector: 'jhi-weapons-detail',
  templateUrl: './weapons-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class WeaponsDetailComponent {
  weapons = input<IWeapons | null>(null);

  previousState(): void {
    window.history.back();
  }
}
