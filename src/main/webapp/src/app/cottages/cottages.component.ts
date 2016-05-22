import {Component} from 'angular2/core';
import {RouterOutlet, RouteConfig} from 'angular2/router';
import {CottagesList} from "./cottages-list/cottages-list.component";
import {CottageDetails} from "./cottage-details/cottage-details.component";
import {Tabs} from "./tabs/tabs.component";
import {Reservations} from './reservations/reservations.component';

@Component({
    selector : 'cottages',
    template : require('./cottages.component.html'),
    styleUrls : [require('./cottages.component.scss')],
    providers : [],
    directives : [RouterOutlet, Tabs],
    pipes : []
})
@RouteConfig([
    {path : '/', name : 'CottagesList', component : CottagesList, useAsDefault : true},
    {path : '/Cottage/:id', name : 'CottageDetails', component : CottageDetails},
    {path : '/Reservations', name : 'Reservations', component : Reservations}
])
export class Cottages {
    options: any;

    constructor() {
        this.options = [{
            name : 'Cottages',
            link: 'CottagesList'
        }, {
            name: 'Reservations',
            link: 'Reservations'
        }];
    }

}
