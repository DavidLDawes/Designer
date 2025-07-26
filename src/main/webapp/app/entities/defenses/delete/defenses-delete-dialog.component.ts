import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDefenses } from '../defenses.model';
import { DefensesService } from '../service/defenses.service';

@Component({
  templateUrl: './defenses-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DefensesDeleteDialogComponent {
  defenses?: IDefenses;

  protected defensesService = inject(DefensesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.defensesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
