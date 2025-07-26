import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEngines } from '../engines.model';

@Component({
  selector: 'jhi-engines-detail',
  templateUrl: './engines-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EnginesDetailComponent {
  engines = input<IEngines | null>(null);

  previousState(): void {
    window.history.back();
  }
}
