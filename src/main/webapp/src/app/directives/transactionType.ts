import { Directive, ElementRef, Input } from 'angular2/core';
import {TransactionType} from '../models/enums/transactioType'

@Directive({
    selector: '[cb-transactionType]'
})
export class TransactionTypeDirective {
    el:ElementRef;

    @Input('cb-transactionType') set transactioType(type:number) {
        this.el.nativeElement.innerHTML = TransactionType[type]
    }

    constructor(el:ElementRef) {
        this.el = el;
    }
}