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
    reservations = [{
        cottage: 'Cottage 22',
        user: 'Jonas',
        dateFrom: '2015-02-02',
        dateTo: '2015-02-09'
    }];
    //constructor() {}
}
