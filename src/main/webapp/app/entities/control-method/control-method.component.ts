import { Component, OnInit } from '@angular/core';
import {ResolutionCondition} from "app/entities/resolution/resolution.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AreaService} from "app/entities/area/area.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ResulutionService} from "app/entities/resolution/resulution.service";
import {JhiEventManager} from "ng-jhipster";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {REGEX_CODE, REGEX_NAME} from "app/shared/constants/validate";
import {CommonStatus} from "app/shared/model/enumerations/common-status.model";
import {ControlMethod} from "app/entities/control-method/control.model";
import {ControlMethodService} from "app/entities/control-method/control-method.service";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";

@Component({
  selector: 'jhi-control-method',
  templateUrl: './control-method.component.html',
  styleUrls: ['./control-method.component.scss']
})
export class ControlMethodComponent implements OnInit {
  indexPage = 0;
  page: object = {};
  size: number = ITEMS_PER_PAGE;
  controlMethodList: ControlMethod[] = [];
  controlMethod: ControlMethod = {};
  controlMethodSearch: ControlMethod = {};
  formControlMethod!: FormGroup;
  formSearch!: FormGroup;
  err001 = false;
  success = false;
  danger = false;
  active:string = '';
  loading = false;

  constructor(
    protected areaService: AreaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected controlMethodService:ControlMethodService,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder,
    protected modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.initFormSearch();
    this.initFormResolution();
    this.pagination();
  }

  initFormResolution() {
    this.formControlMethod = this.fb.group({
      id: null,
      code: ["", [Validators.required, Validators.pattern(REGEX_CODE)]],
      name: ["", [Validators.required, Validators.maxLength(255),Validators.pattern(REGEX_NAME)]],
      status: 1,
      description: ["", Validators.maxLength(1000)],
      createdDate: null,
      createUser: null,
      updatedDate: null,
      updatedUser: null,
    });
  }

  initFormSearch() {
    this.formSearch = this.fb.group({
      code: ["", [Validators.required, Validators.maxLength(200)]],
      name: ["", [Validators.required, Validators.maxLength(200)]],
      status: [0, [Validators.required, Validators.maxLength(200)]],
    });
  }

  search() {
    this.indexPage = 0;
    this.addValueFormSearch();
    this.pagination();
  }

  addValueFormSearch() {
    const formValue = this.formSearch.value;
    this.controlMethodSearch.code = formValue.code.trim();
    this.controlMethodSearch.name = formValue.name.trim();
    this.controlMethodSearch.status = formValue.status == 0 ? null : formValue.status == '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE
  }

  addValueFormToResolution() {
    const formValue = this.formControlMethod.value;
    this.controlMethod.id = formValue.id;
    this.controlMethod.code = formValue.code.trim();
    this.controlMethod.name = formValue.name.trim();
    this.controlMethod.description = formValue.description.trim();
    this.controlMethod.status = formValue.status === '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE;
    this.controlMethod.createdDate = formValue.createdDate
    this.controlMethod.createUser = formValue.createUser;
    this.controlMethod.updatedDate = formValue.updatedDate;
    this.controlMethod.updatedUser = formValue.updatedUser;
  }

  addResolutionToForm() {
    this.formControlMethod.patchValue({
      id: this.controlMethod.id,
      code: this.controlMethod.code,
      name: this.controlMethod.name,
      status: this.controlMethod.status === CommonStatus.ACTIVE ? 1 : 2,
      createdDate: this.controlMethod.createdDate,
      createUser: this.controlMethod.createUser,
      updatedDate: this.controlMethod.updatedDate,
      updatedUser: this.controlMethod.updatedUser,
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
    this.active = "Thêm mới"
    this.err001 = false;
    this.initFormResolution();
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
    this.controlMethodService.getList(this.indexPage, this.controlMethodSearch, this.size).subscribe(value => {
      this.loading = false
      this.page = value.object;
      this.controlMethodList = value.object.content;
    },error => {this.loading = false})

  }

  countStt(): number {
    let totalItemofPage = this.size * (this.indexPage + 1);
    let totalElements = this.page["totalElements"];
    let endItem: number = totalItemofPage < totalElements ? totalItemofPage : totalElements;
    return endItem;
  }

  edit(id: string| undefined, conten: any) {
    if(!id) return;
    let value = this.controlMethodList?.find(value => {
      return value.id == id;
    })
    if (!value) return;
    this.controlMethod = JSON.parse(JSON.stringify(value)) ;
    this.openLg(conten);
    this.active = "Cập nhật"
    this.addResolutionToForm();

  }

  save() {
    if(!this.formControlMethod.valid) return;
    this.addValueFormToResolution();
    this.loading = true;
    if(this.controlMethod.id){
      this.controlMethodService.update(this.controlMethod).subscribe(value => {
        this.loading = false;
        this.danger = false;
        this.success = true;
        this.pagination();
        this.modalService.dismissAll();
        setTimeout(() => this.success = false,3000);
      },error => {
        this.loading = false;
        if(error.error.status == 'ERR_001'){
          this.err001 = true
        }
        this.success = false;
        this.danger = true;
        setTimeout(() => this.danger = false,4000);
      })
    }
    else {
      this.controlMethodService.add(this.controlMethod).subscribe(value => {
        this.loading = false;
        this.controlMethodList.unshift(value.object);
        this.danger = false;
        this.success = true;
        setTimeout(() => this.success = false,3000);
        this.modalService.dismissAll();
      }, error => {
        this.loading = false;
        if (error.error.status == 'ERR_001') {
          this.err001 = true
        }
        this.success = false;
        this.danger = true;
        setTimeout(() => this.danger = false,4000);
      })
    }


  }

}
