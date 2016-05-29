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
    @ViewChild('container') container;
    @ViewChild('dropdown') dropdown;

    private notifications:Array<Notification>;

    constructor(private notificationsService:NotificationsService) {
        notificationsService.getNotifications().subscribe(resp => this.notifications = resp.notifications);
        document.addEventListener('click', this.offClickHandler.bind(this));
    }

    private toggleNotificationsDropdown() {
        console.log("awdawd", this.dropdown);
        if (this.dropdown && this.dropdown.nativeElement) {
            this.dropdown.nativeElement.style.display = "block";
        }
    }

    private handleNotificationClick(event:any) {
        var id = parseInt(event.target.attributes["data-notification-id"].value);
        let model:Notification = this.notifications.find(n => n.id == id);

        this.redirect(model.action);
    }

    private redirect(action:string) {
        
    }

    private offClickHandler(event:any) {
        if (this.dropdown && this.dropdown.nativeElement && this.container && this.container.nativeElement && !this.container.nativeElement.contains(event.target)) {
            this.dropdown.nativeElement.style.display = "none";
        }
    }
}