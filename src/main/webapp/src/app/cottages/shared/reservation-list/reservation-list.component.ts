import {Component} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';


@Component({
    selector: 'reservation-list',
    template: require('./reservation-list.component.html'),
    styleUrls: [],
    providers: [],
    directives: [...ROUTER_DIRECTIVES],
    pipes: [],
    inputs: ['reservations', 'personal']
})
export class ReservationList {
    isButtonActive = false;

    onSubmit() {
        
    }
}
