import {CORE_DIRECTIVES, FORM_DIRECTIVES} from 'angular2/common';
import {Component} from 'angular2/core';
import {Observable} from "rxjs/Observable";

import {Cottage} from '../../shared/cottage.model';
import {RouteParams} from 'angular2/router';
import {Reservation} from "../../shared/reservation.model";
import {ReservationService} from "../../shared/reservation.service";

@Component({
    selector: 'reservation-form',
    template: require('./reservation-form.component.html'),
    styles: [require('./reservation-form.component.scss')],
    providers: [ReservationService],
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES]
})

export class ReservationForm {
    cottage: Cottage;
    reservation = new Reservation();

    constructor(private routeParams: RouteParams,
                private reservationService: ReservationService) {
        let id = this.routeParams.get('id');
        console.log(id);
        this.reservation.cottage = Number(id);
    }

    onSubmit() {
        console.log(this.reservation);
        this.reservationService.reserveCottage(this.reservation)
            .subscribe(
                x => console.log(x),
                error => this.handleError(error));
    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
