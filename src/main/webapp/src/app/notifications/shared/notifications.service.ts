import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {GetNotificationsResponse} from "./notifications-response";

@Injectable()
export class NotificationsService {
    url:string = "/api/notifications";

    constructor(private http:Http) {
    }

    public getNotifications():Observable<GetNotificationsResponse> {
        return this.http.get(`${this.url}`).map(NotificationsService.parse).catch(NotificationsService.handleError);
    }

    private static parse<T>(res:Response):T {
        NotificationsService.ensureSuccess(res);
        return res.json();
    }

    private static ensureSuccess(res:Response) {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }
    }

    private static handleError(error:any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let response = JSON.parse(error._body) || 'Server error';

        //console.error(errMsg); // log to console instead
        return Observable.throw(response);
    }
}
