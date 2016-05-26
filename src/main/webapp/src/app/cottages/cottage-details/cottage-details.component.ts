import {Component} from 'angular2/core';
import {Router, RouteParams} from 'angular2/router';
import {Observable} from 'rxjs/Observable';

import {CottageService} from '../shared/cottages.service';
import {Cottage} from '../shared/cottage.model';
import {ReservationForm} from './reservation-form/reservation-form.component';

@Component({
    selector: 'cottage',
    template: require('./cottage-details.component.html'),
    styles: [require('./cottage-details.component.scss')],
    providers: [CottageService],
    directives: [ReservationForm]
})

export class CottageDetails {
    public cottage = new Cottage();

    constructor(private router: Router,
                private routeParams: RouteParams,
                private cottageService: CottageService) {
        let id = this.routeParams.get('id');
        cottageService.getCottage(id)
            .subscribe(
                cottage => this.cottage = cottage,
                error => this.handleError(error)
            );
    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
