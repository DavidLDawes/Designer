import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWeapons } from '../weapons.model';
import { WeaponsService } from '../service/weapons.service';

@Component({
  templateUrl: './weapons-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WeaponsDeleteDialogComponent {
  weapons?: IWeapons;

  protected weaponsService = inject(WeaponsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.weaponsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
