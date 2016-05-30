import {FormInfoDto} from "../form/shared/formInfo.model";

export class User {
    id: string;
    name: string;
    email: string;
    picture: string;
    group: number;
    fields: Array<FormInfoDto>;
}
