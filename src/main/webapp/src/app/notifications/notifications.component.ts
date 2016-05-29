import {Component, ViewChild} from 'angular2/core';
import {NotificationsService} from "./shared/notifications.service.ts";
import 'rxjs/add/operator/catch';
import {Notification} from "./shared/notification.model.ts";
import {GetNotificationsResponse} from "./shared/notifications-response";

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
    unreadCount:number;

    private notifications:Array<Notification>;

    constructor(private notificationsService:NotificationsService) {
        this.fetchNotifications();
        document.addEventListener('click', this.offClickHandler.bind(this));
    }

    private fetchNotifications() {
        this.notificationsService.getNotifications().subscribe(resp => this.handleResponse(resp));
        this.notificationsService.pollNotifications().subscribe(resp => this.handleResponse(resp));
    }

    private handleResponse(resp:GetNotificationsResponse) {
        this.unreadCount = resp.notifications.filter(n => n.read === false).length;
        this.notifications = resp.notifications;
    }

    private toggleNotificationsDropdown() {
        if (this.container && this.container.nativeElement && this.dropdown && this.dropdown.nativeElement) {
            this.container.nativeElement.classList.add("open");
            this.dropdown.nativeElement.style.display = "block";
        }
    }

    private handleNotificationClick(event:any) {
        let id:number = parseInt(event.target.attributes["data-notification-id"].value);
        let model:Notification = this.notifications.find(n => n.id === id);

        this.markAsRead([id]);
        this.redirectTo(model.action, model.argument);
    }

    private handleMarkAllAsReadClick() {
        var ids = this.notifications.map(n => n.id);

        this.markAsRead(ids);
    }

    private markAsRead(ids:number[]) {
        this.notificationsService.markAsRead(ids).subscribe(r =>
            this.notificationsService.getNotifications().subscribe(resp => this.handleResponse(resp)));
    }

    private redirectTo(action:string, args:string) {
        console.log(action, args);
    }

    private offClickHandler(event:any) {
        if (this.dropdown && this.dropdown.nativeElement && this.container && this.container.nativeElement && !this.container.nativeElement.contains(event.target)) {
            this.dropdown.nativeElement.style.display = "none";
            this.container.nativeElement.classList.remove("open");
        }
    }
}
