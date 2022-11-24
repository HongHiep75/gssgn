import { Injectable } from '@angular/core';
import {SERVER_API_URL} from "../../app.constants";
import {HttpClient} from "@angular/common/http";
import {ControlMethod} from "../control-method/control.model";
import {Observable} from "rxjs";
import {FILE_EX_DEMO_ERR_VAL_COLLATERAL_FREQUENCY} from "app/shared/constants/name.file.ex.constrants";

@Injectable({
  providedIn: 'root'
})
export class ValCollateralFrequencyService {

  public resourceUrl = SERVER_API_URL + 'api/val-collateral-frequency';
  public collateralTypeUrl = SERVER_API_URL + 'api/collateral-type';

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
      .post<any>(`${this.resourceUrl}`, controlMethod);
  }

  update(controlMethod: ControlMethod): Observable<any> {
    return this.http
      .put<any>(`${this.resourceUrl}`, controlMethod);
  }

  getListCollateral(): Observable<any> {
    return this.http.get<any>(`${this.collateralTypeUrl}/code-name`);
  }

  getListCollateralTypeNotFrequency(): Observable<any> {
    return this.http.get<any>(`${this.collateralTypeUrl}/code-name-not-fequency`);
  }

  exportDemoEx(fileName:string): Observable<Blob> {
    return this.http.get(`${this.resourceUrl}/template-tsdb-ex/${fileName}`,{responseType:  'blob'});
  }

  importEx(file:File):Observable<any> {
    const formData = new FormData();
    formData.append("file", file, file.name);
    return this.http.post(this.resourceUrl + "/import-ex" , formData)
  }

  exportErrEx(listErr:any): Observable<Blob> {
    return this.http.post(`${this.resourceUrl}/template-err-ex/`+ FILE_EX_DEMO_ERR_VAL_COLLATERAL_FREQUENCY,listErr
      ,{responseType:  'blob'});
  }
}
