import { Directive, ElementRef, Input } from 'angular2/core';
import {TransactionStatus} from '../shared/enums/transactionStatus.enum.ts';

@Directive({
    selector: '[cb-transactionStatus]'
})
export class TransactionStatusDirective {
    el: ElementRef;

    @Input('cb-transactionStatus') set transactioStatus(type: number) {
        this.el.nativeElement.innerHTML = TransactionStatus[type];
    }

    constructor(el: ElementRef) {
        this.el = el;
    }
}
