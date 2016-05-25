import {Component} from 'angular2/core';
import {User} from './../shared/user.model';
import {UserService} from "./../shared/user.service";
import {RecommendationService} from "./shared/recommendation.service";
import {Member} from "./member/member.component";
import {Recommendation} from "./shared/recommendation.model";
import {RecommendationRequest} from "./recommendation-request/recommendation-request.component";

@Component({
    selector: 'members',
    template: require('./members.component.html'),
    styles: [require('./members.component.scss')],
    providers: [],
    directives: [Member, RecommendationRequest],
    pipes: []
})
export class Members {
    users: Array<User>;
    recommendations: Array<Recommendation>;
    active: string;

    constructor(private userService: UserService, private recommendationService: RecommendationService) {
        userService.getUsers().subscribe(resp => this.users = resp.users);
        recommendationService.getReceivedRecommendations().subscribe(resp => this.recommendations = resp);
    }

    expand(u: User) {
        this.active = u.email;
    }

    recommend(id: String) {
        this.recommendationService.confirmRecommendation(id).subscribe(resp => console.log(resp));
        this.users = null;
        this.recommendations = null;
        this.userService.getUsers().subscribe(resp => this.users = resp.users);
        this.recommendationService.getReceivedRecommendations().subscribe(resp => this.recommendations = resp);
    }
}
