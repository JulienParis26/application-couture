import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TissuComponent } from './list/tissu.component';
import { TissuDetailComponent } from './detail/tissu-detail.component';
import { TissuUpdateComponent } from './update/tissu-update.component';
import { TissuDeleteDialogComponent } from './delete/tissu-delete-dialog.component';
import { TissuRoutingModule } from './route/tissu-routing.module';

@NgModule({
  imports: [SharedModule, TissuRoutingModule],
  declarations: [TissuComponent, TissuDetailComponent, TissuUpdateComponent, TissuDeleteDialogComponent],
  entryComponents: [TissuDeleteDialogComponent],
})
export class TissuModule {}
