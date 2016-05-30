import {Component} from 'angular2/core';
import {Observable} from 'rxjs/Observable';
import {SimpleNotificationsComponent, NotificationsService} from 'angular2-notifications/components';

import {ReservationListItem} from "../shared/reservation-list-item.model";
import {ReservationService} from "../shared/reservation.service";
import {UserService} from "../../shared/user.service";
import {User} from "../../shared/user.model";
import {ReservationList} from "../shared/reservation-list/reservation-list.component";


@Component({
    selector: 'user-reservations',
    template: require('./user-reservations.component.html'),
    styleUrls: [require('./user-reservations.component.scss')],
    providers: [ReservationService, UserService, NotificationsService],
    directives: [ReservationList, SimpleNotificationsComponent],
    pipes: []
})
export class UserReservations {
    public reservations: ReservationListItem[];
    public options = {
        timeOut: 5000,
        lastOnBottom: true
    };
    public isButtonDisabled = false;
    currentUser: User;

    constructor(private reservationService: ReservationService,
                private userService: UserService,
                private notificationsService: NotificationsService) {
        userService.getUserInfo()
            .subscribe(
                user => {
                    this.currentUser = user;
                    reservationService.getReservations('all')
                        .subscribe(
                            data => this.reservations = this.filterMyReservations(data.reservations),
                            error => this.handleError(error)
                        );
                },
                error => this.handleError(error)
            );

    }

    updateList() {
        this.reservations = [new ReservationListItem()];
        this.reservationService.getReservations('all')
            .subscribe(
                data => this.reservations = this.filterMyReservations(data.reservations),
                error => this.handleError(error)
            );
    }

    public deleteReservation(id: string) {
        this.isButtonDisabled = true;
        this.reservationService.deleteReservation(id)
            .subscribe(
                data => {
                    this.isButtonDisabled = false;
                    this.updateList();
                    this.notificationsService.success('Success', 'Cancel successful');
                },
                error => {
                    this.handleError(error);
                    this.isButtonDisabled = false;
                }
            );
    }

    private filterMyReservations(reservations: ReservationListItem[]) {
        return reservations
            .filter(reservation => reservation.user.id === this.currentUser.id);
    }

    private handleError(errors: any) {
        errors.forEach(error => this.notificationsService.error('Error', error.Message));
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = errors.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
