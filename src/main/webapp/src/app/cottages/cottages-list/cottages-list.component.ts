import {Component} from 'angular2/core';
import {Cottage} from "../shared/cottage.model.ts";
import {CottageService} from "../shared/cottages.service.ts";
import {UserService} from "../../shared/user.service.ts";


@Component({
    selector: 'cottages-list',
    template: require('./cottages-list.component.html'),
    styles: [require('./cottages-list.component.scss')],
    providers: [],
    directives: [],
    pipes: []
})
export class CottagesList {
    cottages: Array<Cottage>;
    isAdministrator: boolean;

    constructor(private cottageService: CottageService, private userService: UserService) {
        cottageService.getAllCottages().subscribe(resp => this.cottages = resp);
        userService.hasRole('administrator').subscribe(resp => this.isAdministrator = resp);
    }
}
