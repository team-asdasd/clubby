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
    @ViewChild('infoTable') infoTable;

    user: User;
    recommendationPopup: boolean = false;
    requestingRecommendationState: String = "default";
    isCandidate: boolean = false;

    constructor(userService: UserService, private recommendationService: RecommendationService) {
        userService.getUserInfo().subscribe(user => this.initUser(user));
    }

    initUser(user: User) {
        this.user = user;

        if (user["roles"].indexOf("candidate") > -1) {
            this.isCandidate = true;
        }
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

    submitChanges() {

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
