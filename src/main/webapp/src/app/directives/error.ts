import {Component, Input} from 'angular2/core';

@Component({
    selector: 'cb-error',
    directives: [],
    template: require('./templates/errorTemplate.html')
})
export class ErrorDirective {
    @Input() code: number;
}
