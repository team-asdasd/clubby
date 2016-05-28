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
    @ViewChild('notificationMessage') notificationMessage;
    @ViewChild('infoTable') infoTable;
    @ViewChild('name') name;
    @ViewChild('email') email;
    @ViewChild('password') password: any;
    @ViewChild('password2') password2: any;

    user: User;
    recommendationPopup: boolean = false;
    requestingRecommendationState: String = "default";
    loadingState: String = "waiting";
    isCandidate: boolean = false;

    constructor(private userService: UserService, private recommendationService: RecommendationService) {
        userService.getUserInfo().subscribe(user => this.initUser(user));
    }

    initUser(user: User) {
        this.user = user;

        this.loadingState = "done";

        this.notificationMessage.nativeElement.hidden = false;

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

        this.notificationMessage.nativeElement.hidden = true;

        var fieldsValues: any = document.getElementsByClassName("fieldsValues");

        var newInfo: any = {
            id: this.user.id,
            name: this.name.nativeElement.value,
            email: this.email.nativeElement.value,
            fields: []
        };

        for (var i = 0; i < this.user.fields.length; i++) {

            newInfo.fields.push({
                name: this.user.fields[i].name,
                value: fieldsValues[i].value
            });
        }

        var pass1 = this.password.nativeElement.value;
        var pass2 = this.password2.nativeElement.value;

        if (pass1.length > 0 || pass2.length > 0) {
            newInfo.password = pass1;
            newInfo.passwordConfirm = pass2;
        }

        var body: any = JSON.stringify(newInfo);

        this.loadingState = "waiting";
        this.userService.patch(body).subscribe(resp => this.handlePatchResponse(resp),
                                               err => this.handlePatchResponse(err));
    }

    handlePatchResponse(resp: any) {

        var message = "";

        if (resp.Errors) {
            for (var i = 0; i < resp.Errors.length; i++) {
                message += resp.Errors[i].Message + "!<br>";
            }
        } else {
            message = "The profile has been successfully updated!";
        }

        // Unhide when the user is loaded
        this.notificationMessage.nativeElement.hidden = true;
        this.notificationMessage.nativeElement.innerHTML = message;

        this.userService.getUserInfo().subscribe(user => this.initUser(user));
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
