import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITissu } from '../tissu.model';
import { TissuService } from '../service/tissu.service';

@Component({
  templateUrl: './tissu-delete-dialog.component.html',
})
export class TissuDeleteDialogComponent {
  tissu?: ITissu;

  constructor(protected tissuService: TissuService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tissuService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
