import {FormInfoDto} from "../form/shared/formInfo.model";

export class User {
    name: string;
    email: string;
    picture: string;
    fields: Array<FormInfoDto>;
}
