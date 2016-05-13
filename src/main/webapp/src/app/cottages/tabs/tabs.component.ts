import {Component} from 'angular2/core';
import {RouteData, Router, ROUTER_DIRECTIVES} from 'angular2/router';


@Component({
    selector : 'tabs',
    template : require('./tabs.component.html'),
    styles : [require('./tabs.component.scss')],
    inputs : ['options'],
    directives: [ROUTER_DIRECTIVES]
})

export class Tabs {
    options: any;

    constructor(private _data: RouteData, private router: Router) {
        this.options = _data.get('options');
    }

    public isRouteActive(route) {
        return this.router.isRouteActive(this.router.generate(route));
    }
}
