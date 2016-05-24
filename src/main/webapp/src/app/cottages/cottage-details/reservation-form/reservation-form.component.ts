import {CORE_DIRECTIVES, FORM_DIRECTIVES} from 'angular2/common';

import {Component} from 'angular2/core';
import {Cottage} from '../../shared/cottage.model';
import {RouteParams} from 'angular2/router';
import {CottageService} from '../../shared/cottages.service';

@Component({
    selector: 'reservation-form',
    template: require('./reservation-form.component.html'),
    styles: [],
    providers: [],
    directives: [CORE_DIRECTIVES, FORM_DIRECTIVES]
})

export class ReservationForm {
    cottage: Cottage;
    public dt:Date = new Date();

    constructor(private routeParams: RouteParams,
                private cottageService: CottageService) {
        let id = this.routeParams.get('id');
        console.log(id);
        //TODO: handle server error
    }

}
