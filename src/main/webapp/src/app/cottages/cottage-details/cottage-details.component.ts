import {Component} from 'angular2/core';
import {Router, RouteParams} from 'angular2/router';
import {CottageService} from "../shared/cottages.service";
import {Cottage} from "../shared/cottage.model";

@Component({
    selector: 'cottage',
    template: require('./cottage-details.component.html'),
    styles: [require('./cottage-details.component.scss')],
    providers: [CottageService],
    directives: []
})

export class CottageDetails {
    cottage: Cottage;

    constructor(private router:Router,
                private routeParams:RouteParams,
                private cottageService:CottageService) {
        console.log(router);
        this.cottage = new Cottage();
        let id = this.routeParams.get('id');
        cottageService.getCottage(<number>id)
            .subscribe(cottage => {this.cottage = cottage; console.log(cottage)});
        //TODO: handle server error
    }

}