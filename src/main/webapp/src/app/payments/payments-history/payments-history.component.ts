import {Component} from 'angular2/core';
import {PaymentsService} from "../shared/payments.service.ts";
import {MoneyTransaction} from "../shared/moneyTransaction.model.ts";
import {TransactionStatusDirective} from "../directives/transactionStatus.ts";
import {ErrorDirective} from "../../directives/error.ts";


@Component({
    selector: 'payments-history',
    template: require('./payments-history.component.html'),
    styles: [require('./payments-history.component.scss')],
    providers: [],
    directives: [TransactionStatusDirective, ErrorDirective],
    pipes: []
})
export class HistoryPayments {
    transactions: Array<MoneyTransaction>;
    code: number;
    loading: boolean;

    constructor(private paymentsService: PaymentsService) {
        this.loading = true;
        paymentsService.getHistroyPayments().subscribe(
            resp => {
                this.transactions = resp;
                this.loading = false;
            },
            error => {
                this.code = error.status;
                this.loading = false;
            }
        );
    }
}
