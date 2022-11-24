import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AreaService} from "app/entities/area/area.service";
import {ActivatedRoute, Router} from "@angular/router";
import {JhiEventManager} from "ng-jhipster";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {REGEX_CODE, REGEX_DAY, REGEX_MONTH} from "app/shared/constants/validate";
import {CommonStatus} from "app/shared/model/enumerations/common-status.model";
import {ApplySanction} from "app/entities/apply-sanction/apply-sancion.model";
import {ApplySancionService} from "app/entities/apply-sanction/apply-sancion.service";
import {ITEMS_PER_PAGE} from "app/shared/constants/pagination.constants";

@Component({
  selector: 'jhi-apply-sanction',
  templateUrl: './apply-sanction.component.html',
  styleUrls: ['./apply-sanction.component.scss']
})
export class ApplySanctionComponent implements OnInit {

  indexPage = 0;
  page: object = {};
  size: number = ITEMS_PER_PAGE;
  applySanctionList: ApplySanction[] = [];
  applySanction: ApplySanction = {};
  applySanctionSearch: ApplySanction = {};
  formApplySanction!: FormGroup;
  err001 = false;
  success = false;
  danger = false;
  active:string = '';
  loading = false;

  constructor(
    protected areaService: AreaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected applySanctionService:ApplySancionService,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder,
    protected modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.initFormApplySanction();
    this.pagination();
  }

  initFormApplySanction() {
    this.formApplySanction = this.fb.group({
      id: null,
      code: ["", [Validators.required, Validators.pattern(REGEX_CODE)]],
      applyDate: ["", [Validators.required,Validators.pattern(REGEX_DAY)]],
      applyMonth: ["", [Validators.required, Validators.pattern(REGEX_MONTH)]],
      ratingStatus:["Tuân thủ", Validators.required],
      status: 1,
      createdDate: null,
      createUser: null,
      updatedDate: null,
      updatedUser: null,
    });
  }

  addValueFormToApplySanction() {
    const formValue = this.formApplySanction.value;
    this.applySanction.id = formValue.id;
    if(this.active == "Thêm mới") {
      this.applySanction.code = formValue.code.trim();
    }
    this.applySanction.ratingStatus = formValue.ratingStatus;
    this.applySanction.applyDate = formValue.applyDate;
    this.applySanction.applyMonth = formValue.applyMonth;
    this.applySanction.interestAdjust = formValue.ratingStatus === 'Tuân thủ'?"Giảm":"Tăng";
    this.applySanction.reportDate = "Ngày cuối cùng của tháng",
    this.applySanction.status = formValue.status === '1' ? CommonStatus.ACTIVE : CommonStatus.INACTIVE;
    this.applySanction.createdDate = formValue.createdDate
    this.applySanction.createUser = formValue.createUser;
    this.applySanction.updatedDate = formValue.updatedDate;
    this.applySanction.updatedUser = formValue.updatedUser;
  }

  addApplySanctionToForm() {
    this.formApplySanction.patchValue({
      id: this.applySanction.id,
      code: this.applySanction.code,
      applyDate: this.applySanction.applyDate,
      applyMonth: this.applySanction.applyMonth,
      ratingStatus: this.applySanction.ratingStatus,
      status: this.applySanction.status === CommonStatus.ACTIVE ? 1 : 2,
      createdDate: this.applySanction.createdDate,
      createUser: this.applySanction.createUser,
      updatedDate: this.applySanction.updatedDate,
      updatedUser: this.applySanction.updatedUser,
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
    this.initFormApplySanction();
    this.modalService.open(content, {centered: true, size: "xl"});

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
    this.applySanctionService.getList(this.indexPage, this.size).subscribe(value => {
      this.loading = false
      this.page = value.object;
      this.applySanctionList = value.object.content;
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
    let value = this.applySanctionList?.find(value => {
      return value.id == id;
    })
    if (!value) return;
    this.applySanction = JSON.parse(JSON.stringify(value)) ;
    this.openLg(conten);
    this.active = "Cập nhật"
    this.addApplySanctionToForm();

  }

  save() {
    if(!this.formApplySanction.valid) return;
    this.addValueFormToApplySanction();
    this.loading = true;
    if(this.applySanction.id){
      console.log(this.applySanction);
      this.applySanctionService.update(this.applySanction).subscribe(value => {
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
      this.applySanctionService.add(this.applySanction).subscribe(value => {
        this.loading = false;
        this.applySanctionList.unshift(value.object);
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
