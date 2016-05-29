import {Component, ViewChild} from 'angular2/core';
import {NotificationsService} from "./shared/notifications.service.ts";
import 'rxjs/add/operator/catch';
import {Notification} from "./shared/notification.model.ts";

@Component({
    selector: 'notifications',
    template: require('./notifications.component.html'),
    styles: [require('./notifications.component.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class Notifications {
    private notifications:Array<Notification>;

    constructor(private notificationsService:NotificationsService) {
        notificationsService.getNotifications().subscribe(resp => this.notifications = resp.notifications);
    }

    toggleNotificationsDropdown() {
        console.log("awdawd");
    }
}
