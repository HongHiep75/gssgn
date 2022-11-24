import {Injectable} from '@angular/core';
import {SERVER_API_URL} from "../../app.constants";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ControlMethod} from "./control.model";

@Injectable({
  providedIn: 'root'
})
export class ControlMethodService {

  public resourceUrl = SERVER_API_URL + 'api/control-method';

  constructor(protected http: HttpClient) {
  }

  getList(page: number, controlMethod: ControlMethod, size: number): Observable<any> {
    return this.http
      .put<any>
      (`${this.resourceUrl}/get-list?page=${page}&size=${size}`
        , controlMethod);
  }

  add(controlMethod: ControlMethod): Observable<any> {
    return this.http
      .post<any>
      (`${this.resourceUrl}`, controlMethod);
  }

  update(controlMethod: ControlMethod): Observable<any> {
    return this.http
      .put<any>
      (`${this.resourceUrl}`, controlMethod);

  }
}
