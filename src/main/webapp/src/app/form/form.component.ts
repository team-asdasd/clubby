import {Component, Input} from 'angular2/core';
import {FormInfoDto} from "./shared/formInfo.model";

@Component({
    selector: 'form',
    template: require('./form.component.html'),
    styles: [require('./form.component.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class Form {
    @Input() fields: Array<FormInfoDto>;
    @Input() editable: boolean;
}
