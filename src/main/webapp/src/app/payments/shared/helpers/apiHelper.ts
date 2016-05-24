import {Response} from 'angular2/http';
import {Observable} from "../../../../../node_modules/rxjs/Observable";

export class ApiHelper {
    public static parse<T>(res: Response): T {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }

        return res.json();
    }

    public static handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        console.error(error); // log to console instead
        return Observable.throw(error);
    }
}
