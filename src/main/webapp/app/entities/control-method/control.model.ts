import {CommonStatus} from "../../shared/model/enumerations/common-status.model";
import {Moment} from "moment";

export interface IControlMethod{
  id?: string;
  code?: string;
  name?: string;
  status?: CommonStatus|null;
  description?:string|null;
  createdDate?: Moment;
  createUser?: string;
  updatedDate?: Moment;
  updatedUser?: string;
}

export class ControlMethod implements IControlMethod {
  constructor(
    public id?: string,
    public code?: string,
    public name?: string,
    public status?: CommonStatus|null,
    public description?:string,
    public createdDate?: Moment,
    public createUser?: string,
    public updatedDate?: Moment,
    public updatedUser?: string
  ) {}
}
