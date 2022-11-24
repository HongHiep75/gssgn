import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {SERVER_API_URL} from "../../app.constants";
import {ProductConditionModel} from "./product-condition.model";
import {Observable} from "rxjs";
import {
  FILE_EX_DEMO_ERR_PRODUCT_CONDITION,
  FILE_EX_DEMO_ERR_VAL_COLLATERAL_FREQUENCY
} from "app/shared/constants/name.file.ex.constrants";


@Injectable({
  providedIn:'root'
})
export class ProductConditionService{

  public resourceUrl = SERVER_API_URL + 'api/product-condition';
  constructor(protected http: HttpClient) {
  }

  getList(page:number, productConditionModel: ProductConditionModel, size:number):Observable<any>{
    return this.http
      .put<any>(`${this.resourceUrl}/get-list-product-condition?page=${page}&size=${size}`, productConditionModel);
  }

  addProductCondition(productConditionModel: ProductConditionModel):Observable<any>{
    return this.http
      .post<any>( `${this.resourceUrl}`, productConditionModel)
  }
  updateProductCondition(productConditionModel: ProductConditionModel):Observable<any>{
    return this.http
      .put<any>( `${this.resourceUrl}`, productConditionModel)
  }
  getListProductType():Observable<any>{
    return this.http.get(`${this.resourceUrl}/get-list-product-type`);
  }
  exportDemoEx(fileName:string): Observable<Blob> {
    return this.http.get(`${this.resourceUrl}/template-dksp-ex/${fileName}`,{responseType:  'blob'});
  }
  importEx(file:File):Observable<any> {
    const formData = new FormData();
    formData.append("file", file, file.name);
    return this.http.post(this.resourceUrl + "/import-excel" , formData)
  }
  exportErrEx(listErr:any): Observable<Blob> {
    return this.http.post(`${this.resourceUrl}/template-err-ex/`+ FILE_EX_DEMO_ERR_PRODUCT_CONDITION,listErr
      ,{responseType:  'blob'});
  }
}
