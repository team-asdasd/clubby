import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from 'rxjs/Observable';
import {Reservation} from './reservation.model';

@Injectable()
export class ReservationService {
    url: string = '/api/reservation';

    constructor(private http: Http) {
    }

    public reserveCottage(reservation: Reservation): Observable<any> {
        console.log(reservation);
        return this.http
            .post(this.url, JSON.stringify(reservation))
            .map(this.parse)
            .catch(this.handleError);
    }


    private parse(res: Response): any {
        if (res.status < 200 || res.status >= 300) {
            throw new Error('Bad response status: ' + res.status);
        }

        return res.json();
    }

    private handleError(error: any) {
        // In a real world app, we might send the error to remote logging infrastructure
        let errMsg = error.message || 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}
