import {Component} from 'angular2/core';
import {Router, RouteParams} from 'angular2/router';
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
    cottage: Cottage;

    constructor(private router: Router,
                private routeParams: RouteParams,
                private cottageService: CottageService) {
        this.cottage = new Cottage();
        let id = this.routeParams.get('id');
        cottageService.getCottage(id)
            .subscribe(cottage => {
                this.cottage = cottage;
            });
        //TODO: handle server error
    }

}
