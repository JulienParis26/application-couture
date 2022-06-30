import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITissu, getTissuIdentifier } from '../tissu.model';

export type EntityResponseType = HttpResponse<ITissu>;
export type EntityArrayResponseType = HttpResponse<ITissu[]>;

@Injectable({ providedIn: 'root' })
export class TissuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tissus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tissu: ITissu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tissu);
    return this.http
      .post<ITissu>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tissu: ITissu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tissu);
    return this.http
      .put<ITissu>(`${this.resourceUrl}/${getTissuIdentifier(tissu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tissu: ITissu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tissu);
    return this.http
      .patch<ITissu>(`${this.resourceUrl}/${getTissuIdentifier(tissu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITissu>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITissu[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTissuToCollectionIfMissing(tissuCollection: ITissu[], ...tissusToCheck: (ITissu | null | undefined)[]): ITissu[] {
    const tissus: ITissu[] = tissusToCheck.filter(isPresent);
    if (tissus.length > 0) {
      const tissuCollectionIdentifiers = tissuCollection.map(tissuItem => getTissuIdentifier(tissuItem)!);
      const tissusToAdd = tissus.filter(tissuItem => {
        const tissuIdentifier = getTissuIdentifier(tissuItem);
        if (tissuIdentifier == null || tissuCollectionIdentifiers.includes(tissuIdentifier)) {
          return false;
        }
        tissuCollectionIdentifiers.push(tissuIdentifier);
        return true;
      });
      return [...tissusToAdd, ...tissuCollection];
    }
    return tissuCollection;
  }

  protected convertDateFromClient(tissu: ITissu): ITissu {
    return Object.assign({}, tissu, {
      buyDate: tissu.buyDate?.isValid() ? tissu.buyDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.buyDate = res.body.buyDate ? dayjs(res.body.buyDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tissu: ITissu) => {
        tissu.buyDate = tissu.buyDate ? dayjs(tissu.buyDate) : undefined;
      });
    }
    return res;
  }
}
