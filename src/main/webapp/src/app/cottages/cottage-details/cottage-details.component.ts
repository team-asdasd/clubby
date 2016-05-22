import {Component} from 'angular2/core';
import {Router} from 'angular2/router';

@Component({
    selector: 'cottage',
    template: require('./cottage-details.component.html'),
    styles: [],
    providers: [],
    directives: []
})

export class CottageDetails {
    constructor(private router: Router) {
        console.log(router);
    }

}