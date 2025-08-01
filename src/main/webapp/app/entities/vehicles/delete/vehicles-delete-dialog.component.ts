import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVehicles } from '../vehicles.model';
import { VehiclesService } from '../service/vehicles.service';

@Component({
  templateUrl: './vehicles-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VehiclesDeleteDialogComponent {
  vehicles?: IVehicles;

  protected vehiclesService = inject(VehiclesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehiclesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
