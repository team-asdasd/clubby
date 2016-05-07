import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {Payment} from "../../models/payment";
import {PaymentResponse} from "../responses/paymentResponse";

@Injectable()
export class PaymentsApi {

    url:string = "/api/payments";

    constructor(private http:Http) {
    }

    public getPaymentInfo(id:string):Observable<Payment> {
        return this.http.get(`${this.url}/${id}`).map(this.parse).catch(this.handleError);
    }

    private parse(res:Response):Payment {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }

        let response:PaymentResponse = res.json();
        let result:Payment = response.paymentInfoDto;

        return result;
    }

    private handleError(error:any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}