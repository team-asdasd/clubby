import {Component} from 'angular2/core';
import {User} from './../shared/user.model.ts';
import {UserApi} from "./../shared/user.service.ts";

@Component({
    selector: 'profile',
    template: require('./profile.component.html'),
    styles: [require('./profile.component.scss')],
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
