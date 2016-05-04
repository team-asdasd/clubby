import {Injectable} from 'angular2/core';
import {User} from "../../models/user";
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";

@Injectable()
export class UserApi {
    url:string = "/api/user";

    constructor(private http:Http) {
    }

    public getUserInfo():Observable<User> {
        return this.http.get(`${this.url}/me`).map(UserApi.parse).catch(UserApi.handleError);
    }

    public hasRole(role:string):Observable<boolean> {
        return this.http.get(`${this.url}/me/hasRole/${role}`).map(UserApi.parse).catch(UserApi.handleError);
    }

    public hasPermission(permission:string):Observable<boolean> {
        return this.http.get(`${this.url}/me/hasPermission/${permission}`).map(UserApi.parse).catch(UserApi.handleError);
    }

    private static parse<T>(res:Response):T {
        UserApi.ensureSuccess(res);

        return res.json();
    }

    private static ensureSuccess(res:Response) {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }
    }

    private static handleError(error:any) {
        let errMsg = error.message || 'Server error';
        console.error(errMsg);
        return Observable.throw(errMsg);
    }
}
