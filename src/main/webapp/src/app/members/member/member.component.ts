import {Component, Input} from 'angular2/core';
import {User} from './../../shared/user.model';
import {Form} from './../../form/form.component';

@Component({
    selector: 'member',
    template: require('./member.component.html'),
    styles: [require('./member.component.scss')],
    providers: [],
    directives: [Form],
    pipes: []
})
export class Member {
    @Input() member: User;
    active: boolean;

    toggle() {
        this.active = !this.active;
    }
}
