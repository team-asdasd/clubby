import {Injectable} from 'angular2/core';
import {Http} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable.d.ts";
import {Payment} from "./payment.model.ts";
import {PaymentResponse} from "./payment.response.ts";
import {MoneyTransaction} from "./moneyTransaction.model.ts";
import {HistoryPaymentsResponse} from "./historyPayments.response.ts";
import {ApiHelper} from "./helpers/apiHelper";

@Injectable()
export class PaymentsService {

    url: string = "/api/payments";

    constructor(private http: Http) {
    }

    public getPaymentInfo(id: string): Observable<Payment> {
        return this.http.get(`${this.url}/${id}`)
            .map(resp => ApiHelper.parse<PaymentResponse>(resp).paymentInfoDto)
            .catch(ApiHelper.handleError);
    }

    public getHistroyPayments(): Observable<Array<MoneyTransaction>> {
        return this.http.get(`${this.url}/my/history`)
            .map(resp => ApiHelper.parse<HistoryPaymentsResponse>(resp).payments)
            .catch(ApiHelper.handleError);
    }

    public pay(paymentId: number): Observable<Array<MoneyTransaction>> {
        return this.http.get(`${this.url}/pay/${paymentId}`)
            .map(resp => ApiHelper.parse<HistoryPaymentsResponse>(resp).payments)
            .catch(ApiHelper.handleError);
    }
}
