import {Component, OnInit} from '@angular/core';
import {AreaService} from "app/entities/area/area.service";
import {ActivatedRoute, Router} from "@angular/router";
import {JhiEventManager} from "ng-jhipster";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ResulutionService} from "app/entities/resolution/resulution.service";
import { ResolutionCondition} from "app/entities/resolution/resolution.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CommonStatus} from "app/shared/model/enumerations/common-status.model";
import {REGEX_CODE, REGEX_NAME} from "app/shared/constants/validate";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";

@Component({
  selector: 'jhi-resolution',
  templateUrl: './resolution.component.html',
  styleUrls: ['./resolution.component.scss']
})
export class ResolutionComponent implements OnInit {

  indexPage = 0;
  page: object = {};
  size: number = ITEMS_PER_PAGE;
  resolutionList: ResolutionCondition[] = [];
  resolution: ResolutionCondition = {};
  resolutionSearch: ResolutionCondition = {};
  formResolution!: FormGroup;
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
    protected resulutionService: ResulutionService,
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
    this.formResolution = this.fb.group({
      id: null,
      code: ["", [Validators.required, Validators.pattern(REGEX_CODE)]],
      name: ["", [Validators.required, Validators.maxLength(255),Validators.pattern(REGEX_NAME)]],
      status: 1,
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
    this.resolutionSearch.code = formValue.code.trim();
    this.resolutionSearch.name = formValue.name.trim();
    this.resolutionSearch.status = formValue.status == 0 ? null : formValue.status == '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE
  }

  addValueFormToResolution() {
    const formValue = this.formResolution.value;
    this.resolution.id = formValue.id;
    this.resolution.code = formValue.code.trim();
    this.resolution.name = formValue.name.trim();
    this.resolution.status = formValue.status === '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE;
    this.resolution.createdDate = formValue.createdDate
    this.resolution.createUser = formValue.createUser;
    this.resolution.updatedDate = formValue.updatedDate;
    this.resolution.updatedUser = formValue.updatedUser;
  }

  addResolutionToForm() {
    this.formResolution.patchValue({
      id: this.resolution.id,
      code: this.resolution.code,
      name: this.resolution.name,
      status: this.resolution.status === CommonStatus.ACTIVE ? 1 : 2,
      createdDate: this.resolution.createdDate,
      createUser: this.resolution.createUser,
      updatedDate: this.resolution.updatedDate,
      updatedUser: this.resolution.updatedUser,
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
    this.resulutionService.getPageResolution(this.indexPage, this.resolutionSearch, this.size).subscribe(value => {
      this.loading = false
      this.page = value.object;
      this.resolutionList = value.object.content;
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
    let newResolution = this.resolutionList?.find(value => {
      return value.id == id;
    })
    if (!newResolution) return;
    this.resolution = JSON.parse(JSON.stringify(newResolution)) ;
    this.openLg(conten);
    this.active = "Cập nhật"
    this.addResolutionToForm();

  }

  save() {
    if(!this.formResolution.valid) return;
    this.addValueFormToResolution();
    this.loading = true;
    if(this.resolution.id){
      this.resulutionService.updateResolution(this.resolution).subscribe(value => {
        this.loading = false;
        this.danger = false;
        this.success = true;
        this.modalService.dismissAll();
        this.pagination();
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
        this.resulutionService.addResolution(this.resolution).subscribe(value => {
          this.loading = false;
          this.resolutionList.unshift(value.object);
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
