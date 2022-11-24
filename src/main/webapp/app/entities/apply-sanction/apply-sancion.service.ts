import { Injectable } from '@angular/core';
import {SERVER_API_URL} from "../../app.constants";
import {HttpClient} from "@angular/common/http";
import {ControlMethod} from "../control-method/control.model";
import {Observable} from "rxjs";
import {ApplySanction} from "./apply-sancion.model";

@Injectable({
  providedIn: 'root'
})
export class ApplySancionService {

  public resourceUrl = SERVER_API_URL + 'api/apply-sanction';

  constructor(protected http: HttpClient) {
  }

  getList(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}?page=${page}&size=${size}`);
  }

  add(value: ApplySanction): Observable<any> {
    return this.http
      .post<any>
      (`${this.resourceUrl}`, value);
  }

  update(value: ApplySanction): Observable<any> {
    return this.http
      .put<any>
      (`${this.resourceUrl}`, value);

  }
}
