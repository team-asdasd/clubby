import {Component} from 'angular2/core';
import {Response} from 'angular2/http';
import {Router, RouterOutlet, RouteConfig, RouteParams, ROUTER_DIRECTIVES, RouteData} from 'angular2/router';
import {PaymentsApi} from "../../services/api/paymentsApi";
import {Payment} from "../../models/payment";
import {BaseResponse} from "../../models/baseResponse";
import {PaymentResponse} from "../../services/responses/paymentResponse";
import {ErrorDirective} from "../../directives/error";
import {ErrorDto} from "../../models/errorDto";
import {ErrorResponse} from "../../models/errorResponse";

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
    loading:boolean;
    succesPayment:boolean;
    errors:Array<ErrorDto>;
    currentDate:Date;

    private _paymentsService:PaymentsApi ;

    constructor(private paymentsService:PaymentsApi,params:RouteParams) {
        this._paymentsService = paymentsService;

        this.currentDate = new Date();
        paymentsService.getPaymentInfo(params.get("id"))
            .subscribe(resp => this.payment = resp,
                        error => {
                            this.failed = true;
                        }
            );
    }

    payClubby(payment:Payment) {
        this.errors = null;
        this.loading = true;

        this._paymentsService.pay(payment.PaymentId)
            .subscribe(resp => {
                    this.succesPayment = true;
                    this.loading = false;
                },
                error => {
                    this.succesPayment = false;
                    this.loading = false
                    let resp:Response = error as Response;
                    if(resp){
                        let errorResponse:ErrorResponse = resp.json();
                        this.errors = errorResponse.Errors;
                    }
                }
            );
    }
}
