import {AfterViewInit, Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';

import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AreaService} from "app/entities/area/area.service";
import {ActivatedRoute, Router} from "@angular/router";
import {JhiEventManager} from "ng-jhipster";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import { REGEX_NUMBER} from "app/shared/constants/validate";
import {CommonStatus} from "app/shared/model/enumerations/common-status.model";
import {ValCollateralFrequency} from "app/entities/val-collateral-frequency/ValCollateralFrequency.model";
import {ValCollateralFrequencyService} from "app/entities/val-collateral-frequency/val-collateral-frequency.service";
import {
   FILE_EX_DEMO_ERR_VAL_COLLATERAL_FREQUENCY,
  FILE_EX_DEMO_VAL_COLLATERAL_FREQUENCY
} from "app/shared/constants/name.file.ex.constrants";
import {MatButtonModule} from "@angular/material/button";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";


@Component({
  selector: 'jhi-val-collateral-frequency',
  templateUrl: './val-collateral-frequency.component.html',
  styleUrls: ['./val-collateral-frequency.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ValCollateralFrequencyComponent implements OnInit {

  indexPage = 0;
  page: object = {};
  size: number = ITEMS_PER_PAGE;
  valCollateralFrequencyList: ValCollateralFrequency[] = [];
  valCollateralFrequency: ValCollateralFrequency = {};
  valCollateralFrequencySearch: ValCollateralFrequency = {};
  formValCollateralFrequency!: FormGroup;
  formSearch!: FormGroup;
  err001 = false;
  success = false;
  danger = false;
  active: string = '';
  loading = false;
  collateralType!: string[];
  test: any;
  collateralTypeNotFrequency!: string[];
  file: File | null = null;
  messErr:string|null = null;
  formImport!: FormGroup;
  validateImport = false;
  valueExErr:any;
  waringErr:any;

  constructor(
    protected areaService: AreaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected valCollateralFrequencyService: ValCollateralFrequencyService,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder,
    public button:MatButtonModule,
    protected modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.pagination();
    this.initFormSearch();
    this.initFormValCollateralFrequency();
    this.getCollateralTypeList();
    this.getCollateralTypeListNotFrequency();
    this.initformImport();
  }

  initFormValCollateralFrequency() {
    this.formValCollateralFrequency = this.fb.group({
      id: null,
      collateralType: null,
      frequency: ["", [Validators.required, Validators.pattern(REGEX_NUMBER)]],
      frequencyUnit: ["DAY", Validators.required],
      status: 1,
      createdDate: null,
      createUser: null,
      updatedDate: null,
      updatedUser: null,
    });
  }
  initformImport() {
    this.formImport = this.fb.group({
      import:['', Validators.required],
      fileName: 'Chọn file...'
    });
  }

  initFormSearch() {
    this.formSearch = this.fb.group({
      collateralType: "",
      frequency: ["", Validators.pattern(REGEX_NUMBER)],
      status: 0,
    });
  }

  search() {
    this.indexPage = 0;
    this.addValueFormSearch();
    this.pagination();
  }

  addValueFormSearch() {
    const formValue = this.formSearch.value;
    this.valCollateralFrequencySearch.collateralType = formValue.collateralType;
    this.valCollateralFrequencySearch.frequency = formValue.frequency;
    this.valCollateralFrequencySearch.status = formValue.status == 0 ? null : formValue.status == '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE
  }

  addValueFormToValCollateralFrequency() {
    const formValue = this.formValCollateralFrequency.value;
    this.valCollateralFrequency.id = formValue.id;
    if (this.active == "Thêm mới") {
      this.valCollateralFrequency.collateralType = formValue.collateralType;
    }
    this.valCollateralFrequency.frequency = formValue.frequency;
    this.valCollateralFrequency.frequencyUnit = formValue.frequencyUnit;
    this.valCollateralFrequency.status = formValue.status === '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE;
    this.valCollateralFrequency.createdDate = formValue.createdDate
    this.valCollateralFrequency.createUser = formValue.createUser;
    this.valCollateralFrequency.updatedDate = formValue.updatedDate;
    this.valCollateralFrequency.updatedUser = formValue.updatedUser;
  }

  addValCollateralFrequencyToForm() {
    this.formValCollateralFrequency.patchValue({
      id: this.valCollateralFrequency.id,
      collateralType: this.valCollateralFrequency.collateralType,
      frequency: this.valCollateralFrequency.frequency,
      frequencyUnit: this.valCollateralFrequency.frequencyUnit,
      status: this.valCollateralFrequency.status === CommonStatus.ACTIVE ? 1 : 2,
      createdDate: this.valCollateralFrequency.createdDate,
      createUser: this.valCollateralFrequency.createUser,
      updatedDate: this.valCollateralFrequency.updatedDate,
      updatedUser: this.valCollateralFrequency.updatedUser,
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

  openLg(content: any) {
    this.valueExErr = false;
    this.active = "Thêm mới"
    this.err001 = false;
    this.initformImport();
    this.initFormValCollateralFrequency();
    this.modalService.open(content, {centered: true, size: "lg"});

  }

  preNextPage(type: string) {
    type == 'pre' ? --this.indexPage
      : type == 'next' ? ++this.indexPage
      : type == 'end' ? this.indexPage = (this.page['totalPages'] - 1) : this.indexPage = 0;
    this.pagination();

  }

  pagination() {
    this.indexPage = this.indexPage < 0 ? 0 : this.indexPage;
    this.loading = true;
    this.valCollateralFrequencyService.getList(this.indexPage, this.valCollateralFrequencySearch, this.size).subscribe(value => {
      this.loading = false
      this.page = value.object;
      this.valCollateralFrequencyList = value.object.content;
    }, error => {
      this.loading = false
    })

  }

  countStt(): number {
    let totalItemofPage = this.size * (this.indexPage + 1);
    let totalElements = this.page["totalElements"];
    let endItem: number = totalItemofPage < totalElements ? totalItemofPage : totalElements;
    return endItem;
  }

  edit(id: string | undefined, conten: any) {
    if (!id) return;
    let value = this.valCollateralFrequencyList?.find(value => {
      return value.id == id;
    })
    if (!value) return;
    this.valCollateralFrequency = JSON.parse(JSON.stringify(value));
    this.openLg(conten);
    this.active = "Cập nhật"
    this.addValCollateralFrequencyToForm();

  }

  save() {
    if (!(this.formValCollateralFrequency.valid && this.formValCollateralFrequency.value.collateralType)) return;
    this.addValueFormToValCollateralFrequency();
    this.loading = true;
    if (this.valCollateralFrequency.id) {
      this.valCollateralFrequencyService.update(this.valCollateralFrequency).subscribe(value => {
        this.loading = false;
        this.danger = false;
        this.success = true;
        this.pagination();
        this.modalService.dismissAll();
        setTimeout(() => this.success = false, 3000);
      }, error => {
        this.loading = false;
        if (error.error.status == 'ERR_001') {
          this.err001 = true
        }
        this.success = false;
        this.danger = true;
        setTimeout(() => this.danger = false, 4000);
      })
    } else {
      this.valCollateralFrequencyService.add(this.valCollateralFrequency).subscribe(value => {
        this.getCollateralTypeListNotFrequency();
        this.loading = false;
        this.valCollateralFrequencyList.unshift(value.object);
        this.danger = false;
        this.success = true;
        setTimeout(() => this.success = false, 3000);
        this.modalService.dismissAll();
      }, error => {
        this.loading = false;
        if (error.error.status == 'ERR_001') {
          this.err001 = true
        }
        this.success = false;
        this.danger = true;
        setTimeout(() => this.danger = false, 4000);
      })
    }
  }

  getCollateralTypeList() {
    this.valCollateralFrequencyService.getListCollateral().subscribe(value => {
      this.collateralType = value.object;
    })
  }

  getCollateralTypeListNotFrequency() {
    this.valCollateralFrequencyService.getListCollateralTypeNotFrequency().subscribe(value => {
      this.collateralTypeNotFrequency = value.object;
    })
  }


  exportDemoEx() {
    this.valCollateralFrequencyService.exportDemoEx(FILE_EX_DEMO_VAL_COLLATERAL_FREQUENCY).subscribe(value => {
      this.downloadEx(FILE_EX_DEMO_VAL_COLLATERAL_FREQUENCY,value);

    },error => {

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
      this.valCollateralFrequencyService.importEx(this.file).subscribe(value => {
        this.validateImport = false;
        this.getCollateralTypeListNotFrequency();
        this.loading = false;
        this.pagination();
        if(value.status === "waring"){
          this.waringErr = value.message;
          setTimeout(() => this.waringErr = false, 3000);
          this.valueExErr = value.object;
        }else {
          this.danger = false;
          this.success = true;
          this.modalService.dismissAll();
          setTimeout(() => this.success = false, 3000);
        }
        this.initformImport();
      },error => {
        this.loading = false;
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
    console.log(this.valueExErr);
    this.valCollateralFrequencyService.exportErrEx(this.valueExErr).subscribe(value => {
      this.loading = false;
      this.downloadEx(FILE_EX_DEMO_ERR_VAL_COLLATERAL_FREQUENCY,value);
    },error => {
      this.loading = false;
      this.setErrMess("Có sự cố phát sinh vui lòng thử lại")
    })
  }
}
