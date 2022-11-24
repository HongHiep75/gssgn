import { Moment } from 'moment';
import { CommonStatus } from 'app/shared/model/enumerations/common-status.model';

export interface IArea {
  id?: number;
  areaCode?: string;
  areaName?: string;
  regionCode?: string;
  status?: CommonStatus;
  createdDate?: Moment;
  createdBy?: string;
  updatedDate?: Moment;
  updatedBy?: string;
}

export class Area implements IArea {
  constructor(
    public id?: number,
    public areaCode?: string,
    public areaName?: string,
    public regionCode?: string,
    public status?: CommonStatus,
    public createdDate?: Moment,
    public createdBy?: string,
    public updatedDate?: Moment,
    public updatedBy?: string
  ) {}
}
