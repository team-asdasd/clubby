import {Injectable} from 'angular2/core';
import {User} from "../../components/about/user";
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {Subscription} from "../../../../node_modules/rxjs/Subscription";

@Injectable()
export class Api {
    url: string = "/api"

    constructor(private http: Http) { }

    public getUserInfo(): Observable<User> {
        return this.http.get(this.url + '/user').map(this.parseUser).catch(this.handleError);
    }

    private parseUser(res: Response): User {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }

        let user : User = res.json();
        return user;
    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
