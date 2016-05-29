import {Component} from 'angular2/core';
import {Observable} from 'rxjs/Observable';

import {ReservationListItem} from "../shared/reservation-list-item.model";
import {ReservationService} from "../shared/reservation.service";
import {UserService} from "../../shared/user.service";
import {User} from "../../shared/user.model";
import {ReservationList} from "../shared/reservation-list/reservation-list.component";


@Component({
    selector: 'user-reservations',
    template: require('./user-reservations.component.html'),
    styleUrls: [require('./user-reservations.component.scss')],
    providers: [ReservationService, UserService],
    directives: [ReservationList],
    pipes: []
})
export class UserReservations {
    reservations: [ReservationListItem];
    currentUser: User;

    constructor(private reservationService: ReservationService,
                private userService: UserService) {
        userService.getUserInfo()
            .subscribe(
                user => {
                    this.currentUser = user;
                    reservationService.getReservations('all')
                        .subscribe(
                            data => this.reservations = data.reservations
                                .filter(reservation => reservation.user.id === user.id),
                            error => this.handleError(error),
                            () => console.log(this.reservations)
                        );
                },
                error => this.handleError(error)
            )

    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
