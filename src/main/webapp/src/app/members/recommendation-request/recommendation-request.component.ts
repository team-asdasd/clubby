import {Component, Input} from 'angular2/core';
import {Recommendation} from './../shared/recommendation.model';
import {User} from "../../shared/user.model";
import {UserService} from "../../shared/user.service";

@Component({
    selector: 'recommendation-request',
    template: require('./recommendation-request.component.html'),
    styles: [require('./recommendation-request.component.scss')],
    providers: [],
    pipes: []
})
export class RecommendationRequest {
    @Input() recommendation: Recommendation;
    user: User;

    constructor(private userService: UserService) {
    }

    ngOnInit() {
        this.userService.getUserById(this.recommendation.userId).subscribe(resp => this.user = resp);
    }

}
