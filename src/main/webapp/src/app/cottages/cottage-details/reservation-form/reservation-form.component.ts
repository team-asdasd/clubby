import {CORE_DIRECTIVES, FORM_DIRECTIVES} from 'angular2/common';
import {Component} from 'angular2/core';
import {Observable} from 'rxjs/Observable';

import {Cottage} from '../../shared/cottage.model';
import {Router, RouteParams} from 'angular2/router';
import {Reservation} from '../../shared/reservation.model';
import {ReservationService} from '../../shared/reservation.service';

@Component({
    selector: 'reservation-form',
    template: require('./reservation-form.component.html'),
    styles: [require('./reservation-form.component.scss')],
    providers: [ReservationService],
    inputs: ['cottage'],
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES]
})

export class ReservationForm {
    cottage: Cottage;
    reservation = new Reservation();
    isButtonActive = true;
    errors = [];
    cottage: Cottage;

    constructor(private routeParams: RouteParams,
                private reservationService: ReservationService,
                private router: Router) {
        // let id = this.routeParams.get('id');
        // console.log(id);
    }

    onSubmit() {
        this.errors = [];
        this.isButtonActive = false;
        console.log(this.cottage);

        this.reservation.cottage = Number(this.cottage.id);
        this.reservation.services = this.cottage.services
            .filter(service =>
                (service.amount === 0 || !service.amount) ? false : true
            )
            .map(service => {
                return {
                    id: service.id,
                    amount: service.amount
                };
            });
        this.reservationService.reserveCottage(this.reservation)
            .subscribe(
                details => {
                    this.router.navigateByUrl(`/Payments/${details.payment}`);
                },
                error => {
                    this.errors = error;
                    this.handleError(error);
                    this.isButtonActive = true;
                });
    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
