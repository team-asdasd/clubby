import {UserApi} from "../../services/api/userApi";
import {Component, OnInit} from 'angular2/core';
import {User} from './../../models/user';

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
    constructor(userService: UserApi) {
        userService.getUserInfo().subscribe(user => this.user = user);
    }

    ngOnInit() {
        console.log('Hello profile');
    }
}
