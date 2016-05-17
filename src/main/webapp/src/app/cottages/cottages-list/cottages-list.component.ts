import {Component} from 'angular2/core';
import {Cottage} from "../shared/cottage.model";
import {CottageService} from "../shared/cottages.service";
import {UserService} from "../../shared/user.service";


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

    constructor(private cottageService: CottageService) {
        cottageService.getAllCottages().subscribe(resp => this.cottages = resp);
    }
}
