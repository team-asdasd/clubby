import {Component, OnInit} from 'angular2/core';
import {User} from './../../models/user';
import {UserApi} from "../../services/api/userApi";

@Component({
    selector: 'profile',
    template: require('./profile.html'),
    styles: [require('./profile.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class Profile {
    user:User;

    constructor(userService:UserApi) {
        userService.getUserInfo().subscribe(user => this.user = user);
    }
}
