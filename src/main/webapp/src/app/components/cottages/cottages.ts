import {Component} from 'angular2/core';
import {Router, RouterOutlet, RouteConfig, ROUTER_DIRECTIVES, RouteData} from 'angular2/router';
import {CottagesList} from "../cottages-list/cottages-list";
import {Tabs} from "../tabs/tabs";
import {Reservations} from '../reservations/reservations';

@Component({
    selector : 'cottages',
    template : require('./cottages.html'),
    styleUrls : [require('./cottages.scss')],
    providers : [],
    directives : [RouterOutlet, Tabs],
    pipes : []
})
@RouteConfig([
    {path : '/', name : 'CottagesList', component : CottagesList, useAsDefault : true},
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
