import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IArea } from 'app/shared/model/area.model';

type EntityResponseType = HttpResponse<IArea>;
type EntityArrayResponseType = HttpResponse<IArea[]>;

@Injectable({ providedIn: 'root' })
export class AreaService {
  public resourceUrl = SERVER_API_URL + 'api/areas';

  constructor(protected http: HttpClient) {}

  create(area: IArea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(area);
    return this.http
      .post<IArea>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(area: IArea): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(area);
    return this.http
      .put<IArea>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IArea>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IArea[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(area: IArea): IArea {
    const copy: IArea = Object.assign({}, area, {
      createdDate: area.createdDate && area.createdDate.isValid() ? area.createdDate.toJSON() : undefined,
      updatedDate: area.updatedDate && area.updatedDate.isValid() ? area.updatedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.updatedDate = res.body.updatedDate ? moment(res.body.updatedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((area: IArea) => {
        area.createdDate = area.createdDate ? moment(area.createdDate) : undefined;
        area.updatedDate = area.updatedDate ? moment(area.updatedDate) : undefined;
      });
    }
    return res;
  }
}
