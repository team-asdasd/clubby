import {Component} from 'angular2/core';
import {Router, RouterOutlet, RouteConfig, RouteParams, ROUTER_DIRECTIVES, RouteData} from 'angular2/router';
import {Tabs} from "../tabs/tabs";
import {PaymentsApi} from "../../services/api/paymentsApi";
import {PendingPayments} from '../payments-pending/payments-pending';
import {PayPayment} from '../payments-pay/payments-pay';
import {HistoryPayments} from '../payments-history/payments-history';

@Component({
    selector: 'payments',
    template: require('./payments.html'),
    styles: [require('./payments.scss')],
    providers: [],
    directives : [RouterOutlet, Tabs],
    pipes: []
})
@RouteConfig([
    {path: '/' , name: 'Pending', component: PendingPayments, useAsDefault: true},
    {path: '/:id', name: 'Pay', component: PayPayment},
    {path: '/history', name: 'History', component: HistoryPayments}
])
export class PaymentsCentral {
    options: any;

    constructor(){
        this.options = [{
            name : 'Pending payments',
            link: 'Pending'
        }, {
            name: 'History payments',
            link: 'History'
        }];
    }
}
