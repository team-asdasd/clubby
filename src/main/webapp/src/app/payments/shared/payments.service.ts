import {Injectable} from 'angular2/core';
import {Http} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {Payment} from "./payment.model.ts";
import {PaymentResponse} from "./payment.response.ts";
import {MoneyTransaction} from "./moneyTransaction.model.ts";
import {HistoryPaymentsResponse} from "./historyPayments.response.ts";
import {ApiHelper} from "./helpers/apiHelper";
import {PendingPaymentsResponse} from "./pendingPayments.response";
import {PaymentsResponse} from "./payments.response";
import {GetBalanceResponse} from "./getBalance.response";

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
        return this.http.get(`${this.url}/me/history`)
            .map(resp => ApiHelper.parse<HistoryPaymentsResponse>(resp).payments)
            .catch(ApiHelper.handleError);
    }

    public getPendingPayments(): Observable<Array<Payment>> {
        return this.http.get(`${this.url}/me/pending`)
            .map(resp => ApiHelper.parse<PendingPaymentsResponse>(resp).pendingPayments)
            .catch(ApiHelper.handleError);
    }

    public pay(paymentId: number): Observable<Array<MoneyTransaction>> {
        return this.http.get(`${this.url}/pay/${paymentId}`)
            .map(resp => ApiHelper.parse<HistoryPaymentsResponse>(resp).payments)
            .catch(ApiHelper.handleError);
    }

    public getBalance() {
        return Observable.interval(10000).switchMap(() => this.http.get(`${this.url}/me/balance`)
            .map(resp => ApiHelper.parse<GetBalanceResponse>(resp).balance)
            .catch(ApiHelper.handleError));
    }

    public getAllPaymentsByType(type: number) {
        return this.http.get(`${this.url}/all/${type}`)
            .map(resp => ApiHelper.parse<PaymentsResponse>(resp).payments)
            .catch(ApiHelper.handleError);
    }
}
