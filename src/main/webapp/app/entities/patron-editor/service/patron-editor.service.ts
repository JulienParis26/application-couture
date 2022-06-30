import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPatronEditor, getPatronEditorIdentifier } from '../patron-editor.model';

export type EntityResponseType = HttpResponse<IPatronEditor>;
export type EntityArrayResponseType = HttpResponse<IPatronEditor[]>;

@Injectable({ providedIn: 'root' })
export class PatronEditorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/patron-editors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(patronEditor: IPatronEditor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patronEditor);
    return this.http
      .post<IPatronEditor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(patronEditor: IPatronEditor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patronEditor);
    return this.http
      .put<IPatronEditor>(`${this.resourceUrl}/${getPatronEditorIdentifier(patronEditor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(patronEditor: IPatronEditor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(patronEditor);
    return this.http
      .patch<IPatronEditor>(`${this.resourceUrl}/${getPatronEditorIdentifier(patronEditor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPatronEditor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPatronEditor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPatronEditorToCollectionIfMissing(
    patronEditorCollection: IPatronEditor[],
    ...patronEditorsToCheck: (IPatronEditor | null | undefined)[]
  ): IPatronEditor[] {
    const patronEditors: IPatronEditor[] = patronEditorsToCheck.filter(isPresent);
    if (patronEditors.length > 0) {
      const patronEditorCollectionIdentifiers = patronEditorCollection.map(
        patronEditorItem => getPatronEditorIdentifier(patronEditorItem)!
      );
      const patronEditorsToAdd = patronEditors.filter(patronEditorItem => {
        const patronEditorIdentifier = getPatronEditorIdentifier(patronEditorItem);
        if (patronEditorIdentifier == null || patronEditorCollectionIdentifiers.includes(patronEditorIdentifier)) {
          return false;
        }
        patronEditorCollectionIdentifiers.push(patronEditorIdentifier);
        return true;
      });
      return [...patronEditorsToAdd, ...patronEditorCollection];
    }
    return patronEditorCollection;
  }

  protected convertDateFromClient(patronEditor: IPatronEditor): IPatronEditor {
    return Object.assign({}, patronEditor, {
      printDate: patronEditor.printDate?.isValid() ? patronEditor.printDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.printDate = res.body.printDate ? dayjs(res.body.printDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((patronEditor: IPatronEditor) => {
        patronEditor.printDate = patronEditor.printDate ? dayjs(patronEditor.printDate) : undefined;
      });
    }
    return res;
  }
}
