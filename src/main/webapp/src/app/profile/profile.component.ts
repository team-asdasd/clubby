import {Component} from 'angular2/core';
import {User} from './../shared/user.model';
import {UserService} from "./../shared/user.service";

@Component({
    selector: 'profile',
    template: require('./profile.component.html'),
    styles: [require('./profile.component.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class Profile {
    user: User;

    constructor(userService: UserService) {
        userService.getUserInfo().subscribe(user => this.user = user);
    }
}
