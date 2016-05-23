import { Directive, ElementRef, Input } from 'angular2/core';
@Directive({
    selector: '[cb-required-optional]'
})
export class RequiredOptionalDirective {
    el: ElementRef;

    @Input('cb-required-optional') set transactioStatus(required: boolean) {
        this.el.nativeElement.innerHTML = required ? 'Required' : 'Optional';
    }

    constructor(el: ElementRef) {
        this.el = el;
    }
}
