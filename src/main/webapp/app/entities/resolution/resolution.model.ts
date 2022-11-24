import {CommonStatus} from "../../shared/model/enumerations/common-status.model";
import {Moment} from "moment";

export interface IResolutionCondition{
  id?: string;
  code?: string;
  name?: string;
  status?: CommonStatus|null;
  createdDate?: Moment;
  createUser?: string;
  updatedDate?: Moment;
  updatedUser?: string;
}

export class ResolutionCondition implements IResolutionCondition {
  constructor(
    public id?: string,
    public code?: string,
    public name?: string,
    public status?: CommonStatus|null,
    public createdDate?: Moment,
    public createUser?: string,
    public updatedDate?: Moment,
    public updatedUser?: string
  ) {}
}
