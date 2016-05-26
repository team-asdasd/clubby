import {Injectable} from 'angular2/core';
import {Http, Response, Headers, RequestOptions} from 'angular2/http';
import {Observable} from "../../../../node_modules/rxjs/Observable";
import {Recommendation} from "./recommendation.model.ts";

@Injectable()
export class RecommendationService {

    receivedUrl: string = "/api/recommendation/received";
    sentUrl: string = "/api/recommendation/sent";
    confirmUrl: string = "/api/recommendation/confirm/";
    sendUrl: string = "/api/recommendation/send/";

    constructor(private http: Http) {
    }

    public getReceivedRecommendations(): Observable<Array<Recommendation>> {
        return this.http.get(this.receivedUrl).map(this.parse).catch(this.handleError);
    }

    public getSentRecommendations(): Observable<Array<Recommendation>> {
        return this.http.get(this.sentUrl).map(this.parse).catch(this.handleError);
    }

    public confirmRecommendation(id: String): Observable<any> {

        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });

        return this.http.post(this.confirmUrl + id, "", options).catch(this.handleError);
    }

    public sendRecommendationRequest(email: String): Observable<any> {

        let headers = new Headers({ 'Content-Type': 'application/json' });
        let options = new RequestOptions({ headers: headers });

        return this.http.post(this.sendUrl + email, "", options).map(this.parse).catch(this.handleError);
    }

    private parse(res: Response): Array<Recommendation> {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }

        let result: Array<Recommendation> = res.json().requests;

        return result;
    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let response  = JSON.parse(error._body) || 'Server error';

        //console.error(errMsg); // log to console instead
        return Observable.throw(response);
    }
}
