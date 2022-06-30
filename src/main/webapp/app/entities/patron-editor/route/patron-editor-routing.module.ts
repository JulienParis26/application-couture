import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PatronEditorComponent } from '../list/patron-editor.component';
import { PatronEditorDetailComponent } from '../detail/patron-editor-detail.component';
import { PatronEditorUpdateComponent } from '../update/patron-editor-update.component';
import { PatronEditorRoutingResolveService } from './patron-editor-routing-resolve.service';

const patronEditorRoute: Routes = [
  {
    path: '',
    component: PatronEditorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatronEditorDetailComponent,
    resolve: {
      patronEditor: PatronEditorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatronEditorUpdateComponent,
    resolve: {
      patronEditor: PatronEditorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatronEditorUpdateComponent,
    resolve: {
      patronEditor: PatronEditorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(patronEditorRoute)],
  exports: [RouterModule],
})
export class PatronEditorRoutingModule {}
