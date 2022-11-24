import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IArea, Area } from 'app/shared/model/area.model';
import { AreaService } from './area.service';

@Component({
  selector: 'jhi-area-update',
  templateUrl: './area-update.component.html',
})
export class AreaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    areaCode: [],
    areaName: [],
    regionCode: [],
    status: [],
    createdDate: [],
    createdBy: [],
    updatedDate: [],
    updatedBy: [],
  });

  constructor(protected areaService: AreaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ area }) => {
      if (!area.id) {
        const today = moment().startOf('day');
        area.createdDate = today;
        area.updatedDate = today;
      }

      this.updateForm(area);
    });
  }

  updateForm(area: IArea): void {
    this.editForm.patchValue({
      id: area.id,
      areaCode: area.areaCode,
      areaName: area.areaName,
      regionCode: area.regionCode,
      status: area.status,
      createdDate: area.createdDate ? area.createdDate.format(DATE_TIME_FORMAT) : null,
      createdBy: area.createdBy,
      updatedDate: area.updatedDate ? area.updatedDate.format(DATE_TIME_FORMAT) : null,
      updatedBy: area.updatedBy,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const area = this.createFromForm();
    if (area.id !== undefined) {
      this.subscribeToSaveResponse(this.areaService.update(area));
    } else {
      this.subscribeToSaveResponse(this.areaService.create(area));
    }
  }

  private createFromForm(): IArea {
    return {
      ...new Area(),
      id: this.editForm.get(['id'])!.value,
      areaCode: this.editForm.get(['areaCode'])!.value,
      areaName: this.editForm.get(['areaName'])!.value,
      regionCode: this.editForm.get(['regionCode'])!.value,
      status: this.editForm.get(['status'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      updatedDate: this.editForm.get(['updatedDate'])!.value
        ? moment(this.editForm.get(['updatedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      updatedBy: this.editForm.get(['updatedBy'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArea>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
