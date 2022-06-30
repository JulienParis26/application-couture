import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPatronEditor, PatronEditor } from '../patron-editor.model';
import { PatronEditorService } from '../service/patron-editor.service';

@Injectable({ providedIn: 'root' })
export class PatronEditorRoutingResolveService implements Resolve<IPatronEditor> {
  constructor(protected service: PatronEditorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatronEditor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((patronEditor: HttpResponse<PatronEditor>) => {
          if (patronEditor.body) {
            return of(patronEditor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PatronEditor());
  }
}
