import {Component} from 'angular2/core';
import {User} from './../shared/user.model';
import {UserService} from "./../shared/user.service";
import {Member} from "./member/member.component";

@Component({
    selector: 'members',
    template: require('./members.component.html'),
    styles: [require('./members.component.scss')],
    providers: [],
    directives: [Member],
    pipes: []
})
export class Members {
    users: Array<User>;
    active: string;

    constructor(userService: UserService) {
        userService.getUsers().subscribe(resp => this.users = resp.users);
    }

    expand(u: User) {
        this.active = u.email;
    }
}
