import {Api} from "../../services/api/api";
import {Component, OnInit} from 'angular2/core';
import {User} from './user';

@Component({
    selector: 'profile',
    template: require('./profile.html'),
    styles: [require('./profile.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class Profile implements OnInit {
    user: User;
    constructor(userService: Api) {
        userService.getUserInfo().subscribe(user => this.user = user);
    }

    ngOnInit() {
        console.log('Hello profile');
    }

}
