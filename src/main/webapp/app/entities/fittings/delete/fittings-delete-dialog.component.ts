import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFittings } from '../fittings.model';
import { FittingsService } from '../service/fittings.service';

@Component({
  templateUrl: './fittings-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FittingsDeleteDialogComponent {
  fittings?: IFittings;

  protected fittingsService = inject(FittingsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fittingsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
