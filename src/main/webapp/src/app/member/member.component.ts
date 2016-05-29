import {Component, ViewChild} from 'angular2/core';
import {RouterOutlet, RouteParams} from 'angular2/router';
import {User} from "../shared/user.model";
import {UserService} from "../shared/user.service";
import {Recommendation} from "../members/shared/recommendation.model";
import {RecommendationService} from "../members/shared/recommendation.service";

@Component({
    selector : 'member',
    template : require('./member.component.html'),
    styles: [require('./member.component.scss')],
    providers : [],
    directives : [RouterOutlet],
    pipes : []
})

export class Member {
    @ViewChild('notificationMessage') notificationMessage;
    member: User;
    recommendations: Array<Recommendation>;
    matchingRecommendation: Recommendation;
    loadingState: String = "waiting";

    constructor(private routeParams: RouteParams, private userService: UserService,  private recommendationService: RecommendationService) {

        let id = this.routeParams.get('id');

        userService.getUserById(parseInt(id)).subscribe(
            user => this.handleUserResponse(user),
            error => this.handleError(error)
        );
    }

    handleUserResponse(user: any) {

        this.member = user;

        this.recommendationService.getReceivedRecommendations().subscribe(
            resp => this.handleAllRecommendationsResponse(resp),
            error => this.handleError(error)
        );
    }


    handleAllRecommendationsResponse(resp: any) {

        this.recommendations = resp;

        for (var i = 0; i < resp.length; i++) {
            if (resp[i].userId === this.member.id) {
                this.matchingRecommendation = resp[i];
            }
        }

        this.notificationMessage.nativeElement.hidden = false;
        this.loadingState = "done";
    }

    sendRecRequest(id: String) {

        this.loadingState = "waiting";

        this.recommendationService.confirmRecommendation(id).subscribe(
            resp => this.handleRecommendingResponse(resp),
            error => this.handleError(error)
        );
    }

    handleRecommendingResponse(user: any) {

        this.notificationMessage.nativeElement.innerHTML = "The user has been recommended";

        this.matchingRecommendation = null;

        this.userService.getUserById(parseInt(this.member.id)).subscribe(
            user => this.handleUserResponse(user),
            error => this.handleError(error)
        );
    }

    handleError(error: any) {

        var message = "";

        for (var i = 0; i < error.Errors.length; i++) {
            message += error.Errors[i].Message + "!<br>";
        }

        this.notificationMessage.nativeElement.innerHTML = message;
        this.notificationMessage.nativeElement.hidden = false;

        if (this.member) {
            this.loadingState = "done";
        } else {
            this.loadingState = "error";
        }
    }
}
