import {CommonStatus} from "../../shared/model/enumerations/common-status.model";
import {Moment} from "moment";

export interface IApplySanction{
  id?: string;
  code?: string;
  applyDate?: string;
  applyMonth?: number;
  interestAdjust?:string;
  ratingStatus?:string;
  reportDate?:string;
  status?: CommonStatus|null;
  createdDate?: Moment;
  createUser?: string;
  updatedDate?: Moment;
  updatedUser?: string;
}

export class ApplySanction implements IApplySanction {
  constructor(
    public id?: string,
    public code?: string,
    public applyDate?:string,
    public ratingStatus?:string,
    public applyMonth?: number,
    public interestAdjust?:string,
    public reportDate?:string,
    public status?: CommonStatus|null,
    public createdDate?: Moment,
    public createUser?: string,
    public updatedDate?: Moment,
    public updatedUser?: string
  ) {}
}
