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
    failed: boolean;

    constructor(private paymentsService: PaymentsService) {
        paymentsService.getHistroyPayments().subscribe(
            resp => this.transactions = resp,
            error => this.failed = true
        );
    }
}
