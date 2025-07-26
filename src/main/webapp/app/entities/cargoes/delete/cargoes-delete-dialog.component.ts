import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICargoes } from '../cargoes.model';
import { CargoesService } from '../service/cargoes.service';

@Component({
  templateUrl: './cargoes-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CargoesDeleteDialogComponent {
  cargoes?: ICargoes;

  protected cargoesService = inject(CargoesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cargoesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
