import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatronEditor } from '../patron-editor.model';
import { PatronEditorService } from '../service/patron-editor.service';

@Component({
  templateUrl: './patron-editor-delete-dialog.component.html',
})
export class PatronEditorDeleteDialogComponent {
  patronEditor?: IPatronEditor;

  constructor(protected patronEditorService: PatronEditorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.patronEditorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
