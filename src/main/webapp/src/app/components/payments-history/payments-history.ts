import {Component} from 'angular2/core';
import {PaymentsApi} from "../../services/api/paymentsApi";
import {MoneyTransaction} from "../../models/moneyTransaction";
import {TransactionStatusDirective} from "../../directives/transactionStatus";
import {ErrorDirective} from "../../directives/error";


@Component({
    selector: 'payments-history',
    template: require('./payments-history.html'),
    styles: [require('./payments-history.scss')],
    providers: [],
    directives: [TransactionStatusDirective, ErrorDirective],
    pipes: []
})
export class HistoryPayments {
    transactions:Array<MoneyTransaction>;
    failed:boolean;

    constructor(private paymentsService:PaymentsApi) {
        paymentsService.getHistroyPayments().subscribe(
            resp => this.transactions = resp,
            error => this.failed = true
        );
    }
}
