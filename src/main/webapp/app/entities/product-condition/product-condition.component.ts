import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {JhiEventManager} from "ng-jhipster";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ProductConditionModel} from "app/entities/product-condition/product-condition.model";
import {ProductConditionService} from "app/entities/product-condition/product-condition.service";
import {CommonStatus} from "app/shared/model/enumerations/common-status.model";
import {
  FILE_EX_DEMO_ERR_PRODUCT_CONDITION,
  FILE_EX_DEMO_PRODUCT_CONDITION,
} from "app/shared/constants/name.file.ex.constrants";
import {REGEX_CODE} from "app/shared/constants/validate";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";

@Component({
  selector: 'jhi-product-condition',
  templateUrl: './product-condition.component.html',
  styleUrls: ['./product-condition.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ProductConditionComponent implements OnInit {
  indexPage = 0;
  page: object = {};
  size: number = ITEMS_PER_PAGE;
  productTypeList: String [] = [];
  productTypeListNotFrequency: String [] = [];
  productConditionList: ProductConditionModel[] = [];
  productCondition: ProductConditionModel = {};
  searchValue: ProductConditionModel = {};
  formProductCondition!: FormGroup;
  formSearch!: FormGroup;
  err001 = false;
  success = false;
  danger = false;
  active:string = '';
  loading = false;
  messErr:string|null = null;
  validateImport = false;
  valueExErr:any;
  formImport!: FormGroup;
  file: File | null = null;
  waringErr:any = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder,
    protected modalService: NgbModal,
    protected productConditionService: ProductConditionService,
  ) { }

  ngOnInit(): void {
    this.pagination();
    this.initFormSearch();
    this.initFormProduct();
    this.getListProductTypeName();
  }
  initFormProduct() {
    this.formProductCondition = this.fb.group({
      id: null,
      code: ["", [Validators.required, Validators.pattern(REGEX_CODE)]],
      productType: null,
      frequency: [0, [Validators.required, Validators.pattern("[0-9]{1,10}")]],
      frequencyUnit: ["MONTH", [Validators.required]],
      day: ["", [Validators.required, Validators.pattern("^((?!(0))[0-9]{1,10})$")]],
      sanction: ["", [ Validators.maxLength(1000)]],
      slipCode: ["", [Validators.required, Validators.maxLength(4000)]],
      note: ["", [ Validators.maxLength(1000)]],
      status: 1,
    });
  }
  initFormSearch() {
    this.formSearch = this.fb.group({
      code: "",
      productType: "",
      status: 0,
    });
  }
  initformImport() {
    this.formImport = this.fb.group({
      import:[null, Validators.required],
      fileName: 'Chọn file...'
    });
  }
  openXl(content: any) {
    this.active = "Thêm mới";
    this.err001 = false;
    this.initFormProduct();
    this.modalService.open(content, {centered: true, size: "xl"});
  }

  openLg(longContent:any) {
    this.valueExErr = false;
    this.initformImport();
    this.modalService.open(longContent, {centered: true, size: 'lg' });
  }

  pagination() {
    this.loading = true
    this.indexPage = this.indexPage < 0 ? 0 : this.indexPage;
    this.productConditionService.getList(this.indexPage, this.searchValue, this.size).subscribe(value => {
      this.loading = false
      this.page = value.object;
      this.productConditionList = value.object.content;
    },error => {
      this.loading = false
    })

  }
  pageItem(value: string) {
    const valueNumber = parseInt(value);
    if (valueNumber > this.size) {
      let ratioSize = Math.ceil(valueNumber / this.size);
      this.indexPage = Math.floor(this.indexPage / ratioSize);
    }
    if (valueNumber < this.size) {
      let ratioSize = Math.floor(this.size / valueNumber);
      this.indexPage = Math.floor(this.indexPage * ratioSize);
    }
    this.size = valueNumber;
    this.pagination();
  }

  countStt(): number {
    let totalItemofPage = this.size * (this.indexPage + 1);
    let totalElements = this.page["totalElements"];
    let endItem: number = totalItemofPage < totalElements ? totalItemofPage : totalElements;
    return endItem;
  }
  preNextPage(type: string) {
    type == 'pre' ? --this.indexPage
      : type == 'next' ? ++this.indexPage
        : type == 'end' ? this.indexPage = (this.page['totalPages'] - 1) : this.indexPage = 0;
    this.pagination();

  }
  search() {
    this.indexPage = 0;
    this.addValueFormSearch();
    this.pagination();
  }

  addValueFormSearch() {
    const formValue = this.formSearch.value;
    this.searchValue.code = formValue.code;
    this.searchValue.productType = formValue.productType;
    this.searchValue.status = formValue.status == 0 ? null : formValue.status == '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE
  }

  addValuesFormtoEntity(){
    const formValue = this.formProductCondition.value;
    this.productCondition.id = formValue.id;
    this.productCondition.code = formValue.code;
    this.productCondition.productType = formValue.productType;
    this.productCondition.frequency = formValue.frequency;
    this.productCondition.frequencyUnit = formValue.frequencyUnit;
    this.productCondition.day = formValue.day;
    this.productCondition.sanction = formValue.sanction;
    this.productCondition.slipCode = formValue.slipCode;
    this.productCondition.note = formValue.note;
    this.productCondition.status = formValue.status == '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE;
  }
  saveProductCondition(){
      if(this.formProductCondition.valid && this.formProductCondition.value.collateralType) return;
      this.addValuesFormtoEntity();
      this.loading = true;
      if(this.productCondition.id == null){
        this.productConditionService.addProductCondition(this.productCondition).subscribe(value => {
          this.loading = false;
          this.productConditionList.unshift(value.object);
          this.danger = false;
          this.success = true;
          this.modalService.dismissAll();
          setTimeout(() => this.success = false,3000);
        }, error => {
          this.loading = false;
          if(error.error.status == 'ERR_001'){
            this.err001 = true
          }
          this.success = false;
          this.danger = true;
          setTimeout(() => this.danger = false,4000);
        })
      }else{
        this.productConditionService.updateProductCondition(this.productCondition).subscribe(value => {
          this.loading = false;
          this.danger = false;
          this.success = true;
          this.pagination();
          setTimeout(() => this.success = false,3000);
          this.modalService.dismissAll();
        }, error => {
          this.loading = false;
          if(error.error.status == 'ERR_001'){
            this.err001 = true
          }
          this.success = false;
          this.danger = true;
          setTimeout(() => this.danger = false,4000);
        })
      }
  }

  addProductConditionToForm(){
      this.formProductCondition.patchValue({
        id: this.productCondition.id,
        code: this.productCondition.code,
        frequency: this.productCondition.frequency,
        frequencyUnit: this.productCondition.frequencyUnit,
        productType: this.productCondition.productType,
        day: this.productCondition.day,
        slipCode: this.productCondition.slipCode,
        sanction: this.productCondition.sanction,
        note: this.productCondition.note,
        status: this.productCondition.status == CommonStatus.ACTIVE ? 1 : 2,
      })
  }
  edit(item: any, content: any) {
    if(!item) return;
    this.productCondition = item;
    this.openXl(content);
    this.active = "Cập nhật"
    this.addProductConditionToForm();
  }

  getListProductTypeName(): void{
    this.productConditionService.getListProductType().subscribe(value => {
      this.productTypeList = value.object;
    })
  }
  exportDemoEx() {
    this.productConditionService.exportDemoEx(FILE_EX_DEMO_PRODUCT_CONDITION).subscribe(value => {
      this.downloadEx(FILE_EX_DEMO_PRODUCT_CONDITION,value);
    },error => {
          this.setErrMess("Có sự cố vui lòng thử lại")
    })
  }

  downloadEx(name:string,value:any){
    const blob = new Blob([value], {type: 'application/vnd.ms-excel'});
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
      window.navigator.msSaveOrOpenBlob(blob);
      return;
    }
    const data = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = data
    link.download = name;
    link.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}))
    setTimeout(() => {
      window.URL.revokeObjectURL(data);
      link.remove();
    }, 100)
  }
  onChange(event:any) {
    this.file = event.target.files[0];
    if(this.file){
      const checkType = this.file?.type !== "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
      if(checkType) {
        this.setErrMess("Chỉ có thể chọn file .xls,.xlsx");
        this.initformImport();
        this.validateImport = false;
        return;
      }
      const checkSize = this.file?.size/1000000;
      if(checkSize > 25) {
        this.initformImport();
        this.setErrMess("Chỉ có thể chọn file nhỏ hơn 25mb");
        this.validateImport = false;
        return;
      }
      this.validateImport = true;
      this.formImport.patchValue({fileName:this.file?.name});
      return;
    }
    this.validateImport = false;
  }

  upload() {
    this.loading = true;
    if(this.validateImport && this.file){
      this.productConditionService.importEx(this.file).subscribe(value => {
        this.pagination()
        this.validateImport = false;
        this.loading = false;
        if(value.status === "waring"){
          this.waringErr = value.message;
          setTimeout(() => this.waringErr = false, 3000);
          this.valueExErr = value.object;
        }else {
          this.danger = false;
          this.success = true;
          setTimeout(() => this.success = false, 3000);
          this.modalService.dismissAll();
        }
        this.initformImport();
      },error => {
        this.loading = false
        this.setErrMess("Có sự cố phát sinh vui lòng thử lại");
      })
    }
  }

  setErrMess(mess:string){
    this.messErr = mess;
    setTimeout(() => this.messErr = null,3000);
  }

  exportErrEx() {
    this.loading = true
    this.productConditionService.exportErrEx(this.valueExErr).subscribe(value => {
      this.loading = false;
      this.downloadEx(FILE_EX_DEMO_ERR_PRODUCT_CONDITION,value);
    },error => {
      this.loading = false;
      this.setErrMess("Có sự cố phát sinh vui lòng thử lại")
    })
  }

}
