import {LineItem} from "./lineItem.model";

export class Payment {
    PaymentId:number;
    PaymentTypeId:number;
    InfoText:string;
    Amount:number;
    Currency:string;
    Required:boolean;
    LineItems:Array<LineItem>
}