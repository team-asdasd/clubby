import {Component} from 'angular2/core';
import {Cottage} from "../../models/cottage";
import {CottageApi} from "../../services/api/cottageApi";


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

    constructor(cottageService:CottageApi) {
        cottageService.getAllCottages().subscribe(resp => this.cottages = resp);
    }
}
