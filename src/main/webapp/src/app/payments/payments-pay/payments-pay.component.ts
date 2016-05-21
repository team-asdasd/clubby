import {Component} from 'angular2/core';
import {Response} from 'angular2/http';
import {RouteParams} from 'angular2/router';
import {PaymentsService} from "../shared/payments.service.ts";
import {Payment} from "../shared/payment.model.ts";
import {ErrorDirective} from "../../directives/error.ts";
import {ErrorDto} from "../shared/error.model.ts";
import {ErrorResponse} from "../shared/error.response.ts";

@Component({
    selector: 'payments-pay',
    template: require('./payments-pay.component.html'),
    styles: [require('./payments-pay.component.scss')],
    providers: [],
    directives: [ErrorDirective],
    pipes: []
})
export class PayPayment {
    payment: Payment;
    failed: boolean;
    loading: boolean;
    succesPayment: boolean;
    errors: Array<ErrorDto>;
    currentDate: Date;

    private _paymentsService: PaymentsService ;

    constructor(private paymentsService: PaymentsService, params: RouteParams) {
        this._paymentsService = paymentsService;

        this.currentDate = new Date();
        paymentsService.getPaymentInfo(params.get("id"))
            .subscribe(resp => this.payment = resp,
                        error => {
                            this.failed = true;
                        }
            );
    }

    payClubby(payment: Payment) {
        this.errors = null;
        this.loading = true;

        this._paymentsService.pay(payment.PaymentId)
            .subscribe(resp => {
                    this.succesPayment = true;
                    this.loading = false;
                },
                error => {
                    this.succesPayment = false;
                    this.loading = false;
                    let resp: Response = error as Response;
                    if(resp) {
                        let errorResponse: ErrorResponse = resp.json();
                        this.errors = errorResponse.Errors;
                    }
                }
            );
    }
}
