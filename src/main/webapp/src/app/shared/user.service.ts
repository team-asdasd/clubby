import {Injectable} from 'angular2/core';
import {User} from "./user.model";
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../node_modules/rxjs/Observable";
import {GetAllUsersResponse} from "../members/shared/getAllUsers.respose";

@Injectable()
export class UserService {
    url: string = "/api/user";

    constructor(private http: Http) {
    }

    public getUserInfo(): Observable<User> {
        return this.http.get(`${this.url}/me`).map(UserService.parse).catch(UserService.handleError);
    }

    public getUsers(): Observable<GetAllUsersResponse> {
        return this.http.get(`${this.url}`).map(UserService.parse).catch(UserService.handleError);
    }

    public hasRole(role: string): Observable<boolean> {
        return this.http.get(`${this.url}/me/hasRole/${role}`).map(UserService.parse).catch(UserService.handleError);
    }

    public hasPermission(permission: string): Observable<boolean> {
        return this.http.get(`${this.url}/me/hasPermission/${permission}`).map(UserService.parse).catch(UserService.handleError);
    }

    private static parse<T>(res: Response): T {
        UserService.ensureSuccess(res);
        return res.json();
    }

    private static ensureSuccess(res: Response) {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }
    }

    private static handleError(error: any) {
        let errMsg = error.message || 'Server error';
        console.error(errMsg);
        return Observable.throw(errMsg);
    }
}
