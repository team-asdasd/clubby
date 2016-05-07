import {Component} from 'angular2/core';
import {Router, RouterOutlet, RouteConfig, RouteParams, ROUTER_DIRECTIVES, RouteData} from 'angular2/router';
import {PaymentsApi} from "../../services/api/paymentsApi";
import {Payment} from "../../models/payment";

@Component({
    selector: 'payments-pay',
    template: require('./payments-pay.html'),
    styles: [require('./payments-pay.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class PayPayment {
    payment:Payment;
    currentDate:Date;

    constructor(private paymentsService:PaymentsApi,params:RouteParams) {
        this.currentDate = new Date();
        paymentsService.getPaymentInfo(params.get("id")).subscribe(resp => this.payment = resp);
    }
}
