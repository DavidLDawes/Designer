import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDefenses } from '../defenses.model';

@Component({
  selector: 'jhi-defenses-detail',
  templateUrl: './defenses-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DefensesDetailComponent {
  defenses = input<IDefenses | null>(null);

  previousState(): void {
    window.history.back();
  }
}
