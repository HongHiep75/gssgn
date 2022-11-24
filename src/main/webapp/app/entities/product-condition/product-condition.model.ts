import {Moment} from "moment";
import {CommonStatus} from "../../shared/model/enumerations/common-status.model";

export interface ProductConditionModel{
  id?: string;
  code?: string;
  day?: number;
  frequency?: number;
  frequencyUnit?: string;
  note?: string;
  productType?: string;
  sanction?: string;
  slipCode ?: string;
  createdDate?: Moment;
  createUser?: string;
  updateUser?: string;
  updateDate?: Moment;
  status?: CommonStatus| null;
}
