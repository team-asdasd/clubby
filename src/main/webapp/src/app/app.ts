import {Component} from 'angular2/core';
import {Router, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import {FORM_PROVIDERS} from 'angular2/common';

import '../style/app.scss';

import {UserApi} from './services/api/userApi';
import {Home} from './components/home/home';
import {Profile} from "./components/profile/profile";
import {Cottages} from "./components/cottages/cottages";
import {CottageApi} from "./services/api/cottageApi";

/*
 * App Component
 * Top Level Component
 */
@Component({
    selector: 'app', // <app></app>
    providers: [...FORM_PROVIDERS, UserApi, CottageApi],
    directives: [...ROUTER_DIRECTIVES],
    pipes: [],
    styles: [require('./app.scss')],
    template: require('./app.html')
})
@RouteConfig([
    {path: '/', component: Home, as: 'Home', useAsDefault: true},
    {path: '/Profile', component: Profile, as: 'Profile'},
    {path: '/Cottages/...', component: Cottages, as: 'Cottages'}
])
export class App {
    url:string = 'https://github.com/preboot/angular2-webpack';

    constructor(private router:Router) {
    }

    public isRouteActive(route) {
        return this.router.isRouteActive(this.router.generate(route));
    }
}
