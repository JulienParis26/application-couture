import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PatronEditorComponent } from './list/patron-editor.component';
import { PatronEditorDetailComponent } from './detail/patron-editor-detail.component';
import { PatronEditorUpdateComponent } from './update/patron-editor-update.component';
import { PatronEditorDeleteDialogComponent } from './delete/patron-editor-delete-dialog.component';
import { PatronEditorRoutingModule } from './route/patron-editor-routing.module';

@NgModule({
  imports: [SharedModule, PatronEditorRoutingModule],
  declarations: [PatronEditorComponent, PatronEditorDetailComponent, PatronEditorUpdateComponent, PatronEditorDeleteDialogComponent],
  entryComponents: [PatronEditorDeleteDialogComponent],
})
export class PatronEditorModule {}
