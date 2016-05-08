import {Component} from 'angular2/core';
import {Router, RouterOutlet, RouteConfig, RouteParams, ROUTER_DIRECTIVES, RouteData} from 'angular2/router';
import {PaymentsApi} from "../../services/api/paymentsApi";
import {Payment} from "../../models/payment";
import {BaseResponse} from "../../models/baseResponse";
import {PaymentResponse} from "../../services/responses/paymentResponse";
import {ErrorDirective} from "../../directives/error";

@Component({
    selector: 'payments-pay',
    template: require('./payments-pay.html'),
    styles: [require('./payments-pay.scss')],
    providers: [],
    directives: [ErrorDirective],
    pipes: []
})
export class PayPayment {
    payment:Payment;
    failed:boolean;
    currentDate:Date;

    constructor(private paymentsService:PaymentsApi,params:RouteParams) {
        this.currentDate = new Date();
        paymentsService.getPaymentInfo(params.get("id"))
            .subscribe(resp => this.payment = resp,
                        error => this.failed = true
            );
    }
}
