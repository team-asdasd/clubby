import {Component} from 'angular2/core';
import {ReservationList} from "../shared/reservation-list/reservation-list.component";


@Component({
    selector: 'all-reservations',
    template: require('./all-reservations.component.html'),
    styleUrls: [require('./all-reservations.component.scss')],
    providers: [],
    directives: [ReservationList],
    pipes: []
})
export class AllReservations {

    //constructor() {}
}
