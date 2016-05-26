import {Component, ViewChild} from 'angular2/core';
import {User} from './../shared/user.model';
import {UserService} from "./../shared/user.service";
import {RecommendationService} from "../members/shared/recommendation.service";
import 'rxjs/add/operator/catch';

@Component({
    selector: 'profile',
    template: require('./profile.component.html'),
    styles: [require('./profile.component.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class Profile {
    @ViewChild('recommendationMessage') recommendationMessage;

    user: User;
    recommendationPopup: boolean = false;
    requestingRecommendationState: String = "default";

    constructor(userService: UserService, private recommendationService: RecommendationService) {
        userService.getUserInfo().subscribe(user => this.user = user);
    }

    toggleRecommendationPopup() {
        this.recommendationPopup = !this.recommendationPopup;
        this.requestingRecommendationState = "default";
        if (this.recommendationMessage) {
            this.recommendationMessage.nativeElement.innerHTML = "";
        }
    }

    sendRecommendationRequest(email: String) {
        this.recommendationService.sendRecommendationRequest(email)
            .subscribe(resp => this.handleRecommendationResponse(resp),
                       err => this.handleError(err));

        this.requestingRecommendationState = "waiting";
    }

    handleRecommendationResponse(resp: any) {
        this.requestingRecommendationState = "done";
        this.recommendationMessage.nativeElement.innerHTML = "The request has been send";
    }

    handleError(resp: any) {
        this.requestingRecommendationState = "done";

        if (resp.Errors) {
            this.recommendationMessage.nativeElement.innerHTML = "Error: " + resp.Errors[0].Message;
        } else {
            this.recommendationMessage.nativeElement.innerHTML = "Error: " + resp;
        }
    }
}
