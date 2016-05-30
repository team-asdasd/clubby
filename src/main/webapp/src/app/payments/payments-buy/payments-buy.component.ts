import {PaymentsService} from "../shared/payments.service";
import {Payment} from "../shared/payment.model";
import {Component, Pipe, PipeTransform} from 'angular2/core';
import {ErrorDirective} from "../../directives/error.ts";

@Pipe({name: 'rows'})
export class RowsPipe implements PipeTransform {
    transform(value, times: string[]) : any {
        var rows = {};
        let current: number = 0;
        let exp: number = parseInt(times[0]);

        if(value) {
            value.forEach(function (o) {
                let index = (current - (current % exp)) / exp;

                rows[index] = rows[index] ? rows[index] : {row: index, columns: []};
                rows[index].columns.push(o);
                current++;
            });
        }
        return Object.keys(rows).map(function (key) {return rows[key]; });
    }
}


@Component({
    selector: 'payments-buy',
    template: require('./payments-buy.component.html'),
    styles: [require('./payments-buy.component.scss')],
    providers: [],
    directives: [ErrorDirective],
    pipes: [RowsPipe]
})
export class BuyClubbyCoins {
    payments: Array<Payment>;
    code: number;
    loading: boolean;

    constructor (private paymentsService: PaymentsService) {
        this.loading = true;

        paymentsService.getAllPaymentsByType(2).subscribe(
            resp => {
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
