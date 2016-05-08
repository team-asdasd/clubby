import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {Payment} from "../../models/payment";
import {PaymentResponse} from "../responses/paymentResponse";
import {MoneyTransaction} from "../../models/moneyTransaction";
import {HistoryPaymentsResponse} from "../responses/historyPaymentsResponse";
import {ApiHelper} from "./helpers/apiHelper"

@Injectable()
export class PaymentsApi {

    url:string = "/api/payments";

    constructor(private http:Http) {
    }

    public getPaymentInfo(id:string):Observable<Payment> {
        return this.http.get(`${this.url}/${id}`)
            .map(resp => ApiHelper.parse<PaymentResponse>(resp).paymentInfoDto)
            .catch(ApiHelper.handleError);
    }

    public getHistroyPayments():Observable<Array<MoneyTransaction>>{
        return this.http.get(`${this.url}/my/history`)
            .map(resp => ApiHelper.parse<HistoryPaymentsResponse>(resp).payments)
            .catch(ApiHelper.handleError);
    }
}