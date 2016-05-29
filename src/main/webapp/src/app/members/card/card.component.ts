import {Component, Input} from 'angular2/core';
import {Router} from 'angular2/router';
import {User} from './../../shared/user.model';
import {Form} from './../../form/form.component';

@Component({
    selector: 'card',
    template: require('./card.component.html'),
    styles: [require('./card.component.scss')],
    providers: [],
    directives: [Form],
    pipes: []
})

export class Card {
    @Input() member: User;
    expanded: boolean = false;

    constructor(private router: Router) {}

    togglePopup() {
        this.expanded = !this.expanded;
    }

    public onSelect(id: String) {
        this.router.navigate( ['Member', { id: id }] );
    }
}
