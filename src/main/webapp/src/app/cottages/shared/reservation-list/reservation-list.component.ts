import {Component, EventEmitter, Output} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';

import {ReservationService} from "../reservation.service";


@Component({
    selector: 'reservation-list',
    template: require('./reservation-list.component.html'),
    styleUrls: [],
    providers: [ReservationService],
    directives: [...ROUTER_DIRECTIVES],
    pipes: [],
    inputs: ['reservations', 'personal', 'isButtonDisabled']
})
export class ReservationList {
    @Output() onDeleteReservation = new EventEmitter<string>();

    onDeleteClick(id: string) {
        this.onDeleteReservation.emit(id);
    }
}
