import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {Cottage} from "../../models/cottage";
import {CottagesResponse} from "../responses/cottagesResponse";

@Injectable()
export class CottageApi {
    url:string = "/api/cottage";

    constructor(private http:Http) {
    }

    public getAllCottages():Observable<Array<Cottage>> {
        return this.http.get(this.url).map(this.parse).catch(this.handleError);
    }

    private parse(res:Response):Array<Cottage> {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }

        let response:CottagesResponse = res.json();
        let result:Array<Cottage> = response.Cottages;

        return result;
    }

    private handleError(error:any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}