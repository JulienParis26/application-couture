import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITissu, Tissu } from '../tissu.model';
import { TissuService } from '../service/tissu.service';

@Injectable({ providedIn: 'root' })
export class TissuRoutingResolveService implements Resolve<ITissu> {
  constructor(protected service: TissuService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITissu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tissu: HttpResponse<Tissu>) => {
          if (tissu.body) {
            return of(tissu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tissu());
  }
}
