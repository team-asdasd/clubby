import {Injectable} from 'angular2/core';
import {User} from "./user.model";
import {Http, Response, Headers, RequestOptions} from 'angular2/http';
import {Observable} from "../../../node_modules/rxjs/Observable";
import {GetAllUsersResponse} from "../members/shared/getAllUsers.response.ts";

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

    public getUserById(id: number): Observable<User> {
        return this.http.get(`${this.url}/${id}`).map(UserService.parse).catch(UserService.handleError);
    }

    public hasRole(role: string): Observable<boolean> {
        return this.http.get(`${this.url}/me/hasRole/${role}`).map(UserService.parse).catch(UserService.handleError);
    }

    public hasPermission(permission: string): Observable<boolean> {
        return this.http.get(`${this.url}/me/hasPermission/${permission}`).map(UserService.parse).catch(UserService.handleError);
    }

    public deleteMe(): Observable<any> {
        return this.http.delete(`${this.url}/me`).map(UserService.parse).catch(UserService.handleError);
    }

    public inviteFriend(email: String, message: String): Observable<any> {

        let body = JSON.stringify({
            "email": email,
            "message": message
        });

        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });

        return this.http.post(`${this.url}/invite`, body, options).map(UserService.parse).catch(UserService.handleError);
    }

    public patch(body: string) : Observable<any> {

        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });

        return this.http.patch(this.url, body, options).catch(UserService.handleError);
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
        // In a real world app, we might send the error to remote logging infrastructure
        let response  = JSON.parse(error._body) || 'Server error';

        //console.error(errMsg); // log to console instead
        return Observable.throw(response);
    }
}
