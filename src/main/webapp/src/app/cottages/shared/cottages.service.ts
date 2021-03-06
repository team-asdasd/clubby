import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {Cottage} from "./cottage.model";

@Injectable()
export class CottageService {
    url: string = "/api/cottage";

    constructor(private http: Http) {
    }

    public getAllCottages(): Observable<Array<Cottage>> {
        return this.http
            .get(this.url)
            .map(this.parseArray)
            .catch(this.handleError);
    }

    public getCottage(id: string): Observable<Cottage> {
        return this.http
            .get(this.url + '/' + id)
            .map(this.parse)
            .catch(this.handleError);
    }

    public getFilteredCottages(query: string): Observable<Array<Cottage>> {
        return this.http
            .get(this.url + query)
            .map(this.parseArray)
            .catch(this.handleError);
    }

    private parseArray(res: Response): Array<Cottage> {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }

        let result: Array<Cottage> = res.json().cottages;
        return result;
    }

    private parse(res: Response): Cottage {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }
        return res.json().cottage;
    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
