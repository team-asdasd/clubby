import {Component} from 'angular2/core';
import {User} from './../shared/user.model';
import {UserService} from "./../shared/user.service";
import {Card} from "./card/card.component.ts";

@Component({
    selector: 'members',
    template: require('./members.component.html'),
    styles: [require('./members.component.scss')],
    providers: [],
    directives: [Card],
    pipes: []
})

export class Members {
    users: Array<User>;
    active: string;

    constructor(private userService: UserService) {
        userService.getUsers().subscribe(resp => this.users = resp.users);
    }

    expand(u: User) {
        this.active = u.email;
    }

    recommend(id: String) {
        this.users = null;
        this.userService.getUsers().subscribe(resp => this.users = resp.users);
    }
}
