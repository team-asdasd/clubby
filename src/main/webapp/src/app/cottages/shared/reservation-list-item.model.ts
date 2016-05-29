import {User} from "../../shared/user.model";
export class ReservationListItem {
    id: number;
    cottage: string;
    user: User;
    dateFrom: Date;
    dateTo: Date;
}