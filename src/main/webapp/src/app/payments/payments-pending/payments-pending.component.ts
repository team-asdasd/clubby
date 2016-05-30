import {Component} from 'angular2/core';
import {PaymentsService} from "../shared/payments.service";
import {Payment} from "../shared/payment.model";
import {ErrorDirective} from "../../directives/error";
import {RequiredOptionalDirective} from "../directives/requiredOptional";

@Component({
    selector: 'payments-pending',
    template: require('./payments-pending.component.html'),
    styles: [require('./payments-pending.component.scss')],
    providers: [],
    directives: [ErrorDirective, RequiredOptionalDirective],
    pipes: []
})
export class PendingPayments {
    payments: Array<Payment>;
    code: number;
    loading: boolean;

    constructor (private paymentsService: PaymentsService) {
        this.loading = true;

        paymentsService.getPendingPayments().subscribe(
            resp => {
                this.code = 200;
                this.payments = resp;
                this.loading = false;
            },
            error => {
                this.code = error.status;
                this.loading = false;
            }
        );
    }
}
