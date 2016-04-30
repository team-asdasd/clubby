import {Component} from 'angular2/core';
import {Cottage} from "../../models/cottage";
import {CottageApi} from "../../services/api/cottageApi";
import {UserApi} from "../../services/api/userApi";


@Component({
    selector: 'cottages-list',
    template: require('./cottages-list.html'),
    styles: [require('./cottages-list.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class CottagesList {
    cottages:Array<Cottage>;
    isAdministrator:boolean;

    constructor(private cottageService:CottageApi, private userService:UserApi) {
        cottageService.getAllCottages().subscribe(resp => this.cottages = resp);
        userService.hasRole('administrator').subscribe(resp => this.isAdministrator = resp);
    }
}
