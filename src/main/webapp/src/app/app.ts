import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';
import {FORM_PROVIDERS} from 'angular2/common';

import '../style/app.scss';

import {Api} from './services/api/api';
import {Home} from './components/home/home';
import {Profile} from "./components/about/profile";

/*
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'app', // <app></app>
  providers: [...FORM_PROVIDERS, Api],
  directives: [...ROUTER_DIRECTIVES],
  pipes: [],
  styles: [require('./app.scss')],
  template: require('./app.html')
})
@RouteConfig([
  {path: '/', component: Home, name: 'Home'},
  {path: '/Profile', component: Profile, name: 'Profile'}
])
export class App {
  url: string = 'https://github.com/preboot/angular2-webpack';

  constructor(public api: Api) {
  }
}
