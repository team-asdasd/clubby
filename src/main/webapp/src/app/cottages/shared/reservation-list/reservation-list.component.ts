import {Component, EventEmitter, Output, Input} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';

import {ReservationService} from "../reservation.service";


@Component({
    selector: 'reservation-list',
    template: require('./reservation-list.component.html'),
    styles: [require('./reservation-list.component.scss')],
    providers: [ReservationService],
    directives: [...ROUTER_DIRECTIVES],
    pipes: [],
    inputs: ['personal', 'isButtonDisabled']
})
export class ReservationList {
    @Output() onDeleteReservation = new EventEmitter<string>();
    @Input() reservations;
    originalReservations: any;

    onDeleteClick(id: string) {
        this.onDeleteReservation.emit(id);
    }
    
    filterReservations(dateFrom: string, dateTo: string) {
        if (!this.originalReservations) {
            this.originalReservations = this.reservations;  //cache original object
        }
        let from = dateFrom ? new Date(dateFrom) : new Date(-8640000000000000); //earliest point in time before sweet baby jesus
        let to = dateTo ? new Date(dateTo) : new Date(8640000000000000); //point in time after sweet baby jesus and the era of men
        this.reservations = this.originalReservations.filter(reservation => {
            return new Date(reservation.dateFrom) >= from && new Date(reservation.dateTo) <= to;
        });
    }
}
