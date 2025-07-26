import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEngines } from '../engines.model';
import { EnginesService } from '../service/engines.service';

@Component({
  templateUrl: './engines-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EnginesDeleteDialogComponent {
  engines?: IEngines;

  protected enginesService = inject(EnginesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enginesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
