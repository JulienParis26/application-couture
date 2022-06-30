import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patron-editor',
        data: { pageTitle: 'coutureApp.patronEditor.home.title' },
        loadChildren: () => import('./patron-editor/patron-editor.module').then(m => m.PatronEditorModule),
      },
      {
        path: 'tissu',
        data: { pageTitle: 'coutureApp.tissu.home.title' },
        loadChildren: () => import('./tissu/tissu.module').then(m => m.TissuModule),
      },
      {
        path: 'seller',
        data: { pageTitle: 'coutureApp.seller.home.title' },
        loadChildren: () => import('./seller/seller.module').then(m => m.SellerModule),
      },
      {
        path: 'patron',
        data: { pageTitle: 'coutureApp.patron.home.title' },
        loadChildren: () => import('./patron/patron.module').then(m => m.PatronModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'coutureApp.project.home.title' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
