import { Injectable } from '@angular/core';
import {SERVER_API_URL} from "../../app.constants";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ResolutionCondition} from "./resolution.model";

@Injectable({
  providedIn: 'root'
})
export class ResulutionService {

  public resourceUrl = SERVER_API_URL + 'api/resolution-condition';

  constructor(protected http: HttpClient) {}

  getPageResolution(page:number,resolutionCondition:ResolutionCondition,size:number):Observable<any>{
    return this.http
      .put<any>
      (  `${this.resourceUrl}/get-list?page=${page}&size=${size}`
        ,resolutionCondition);
  }
  addResolution(resolutionCondition:ResolutionCondition):Observable<any>{
    return this.http
      .post<any>
      (  `${this.resourceUrl}`,resolutionCondition);}

  updateResolution(resolutionCondition:ResolutionCondition):Observable<any>{
    return this.http
      .put<any>
      (  `${this.resourceUrl}`,resolutionCondition);}

}
