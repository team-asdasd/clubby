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
    constructor() {
        //create a fake user
        this.user = {
            fullName: 'Bhja Fo',
            email: 'jojo@gmail.com',
            birthday: 'June 02, 1998'
        };
    }

    ngOnInit() {
        console.log('Hello profile');
    }

}