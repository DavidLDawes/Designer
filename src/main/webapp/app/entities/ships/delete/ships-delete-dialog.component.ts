import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IShips } from '../ships.model';
import { ShipsService } from '../service/ships.service';

@Component({
  templateUrl: './ships-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ShipsDeleteDialogComponent {
  ships?: IShips;

  protected shipsService = inject(ShipsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shipsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
