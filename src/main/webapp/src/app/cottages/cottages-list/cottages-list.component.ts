import {Router} from 'angular2/router';

import {Component} from 'angular2/core';
import {Cottage} from "../shared/cottage.model";
import {CottageService} from "../shared/cottages.service";
import {RoomsSelector} from "./rooms-selector/rooms-selector.component";

@Component({
    selector: 'cottages-list',
    template: require('./cottages-list.component.html'),
    styles: [require('./cottages-list.component.scss')],
    providers: [],
    directives: [RoomsSelector]
})


export class CottagesList {
    cottages: Array<Cottage>;

    constructor(
        private cottageService: CottageService,
        private router: Router) {
        cottageService.getAllCottages().subscribe(resp => this.cottages = resp);
    }

    public filterCottages(title: string, beds, priceFrom: string, priceTo: string, dateFrom: string, dateTo: string) {
        var query = "?";
        query += title.length > 0 ? "title=" + title + "&" : "";
        query += priceFrom ? "priceFrom=" + parseInt(priceFrom) * 100 + "&" : "";
        query += priceTo ? "priceTo=" + parseInt(priceTo) * 100 + "&" : "";
        query += dateFrom ? "dateFrom=" + dateFrom + "&" : "";
        query += dateTo ? "dateTo=" + dateTo + "&" : "";
        query += beds !== "Any" ? "beds=" + beds + "&" : "";
        query = query.substring(0, query.length - 1);
        this.cottageService.getFilteredCottages(query).subscribe(resp => this.cottages = resp);
    }

    public onSelect(cottage: Cottage) {
        console.log('navigating to cottage ', cottage.id);
        this.router.navigate( ['CottageDetails', { id: cottage.id }] );
    }
}
