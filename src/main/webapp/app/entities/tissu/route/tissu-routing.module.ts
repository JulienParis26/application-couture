import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TissuComponent } from '../list/tissu.component';
import { TissuDetailComponent } from '../detail/tissu-detail.component';
import { TissuUpdateComponent } from '../update/tissu-update.component';
import { TissuRoutingResolveService } from './tissu-routing-resolve.service';

const tissuRoute: Routes = [
  {
    path: '',
    component: TissuComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TissuDetailComponent,
    resolve: {
      tissu: TissuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TissuUpdateComponent,
    resolve: {
      tissu: TissuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TissuUpdateComponent,
    resolve: {
      tissu: TissuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tissuRoute)],
  exports: [RouterModule],
})
export class TissuRoutingModule {}
