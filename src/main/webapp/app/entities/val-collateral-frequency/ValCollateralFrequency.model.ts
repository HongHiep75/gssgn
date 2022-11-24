import {CommonStatus} from "../../shared/model/enumerations/common-status.model";
import {Moment} from "moment";

export interface ValCollateralFrequency{
  id?: string;
  collateralType?: string;
  frequency?: number;
  frequencyUnit?:string,
  status?: CommonStatus|null;
  createdDate?: Moment;
  createUser?: string;
  updatedDate?: Moment;
  updatedUser?: string;
}

