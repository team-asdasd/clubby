import {Component} from 'angular2/core';


@Component({
    selector: 'reservation-list',
    template: require('./reservation-list.component.html'),
    styleUrls: [],
    providers: [],
    directives: [],
    pipes: [],
    inputs: ['reservations']
})
export class ReservationList {
    constructor() {
    }
}