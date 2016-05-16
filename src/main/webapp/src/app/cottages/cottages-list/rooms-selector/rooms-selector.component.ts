import {Component, Input, ViewChild} from 'angular2/core';

@Component({
    selector: 'rooms-selector',
    template: require('./rooms-selector.component.html'),
    styles: [require('./rooms-selector.component.scss')]
})

export class RoomsSelector {

    @Input() cottages;
    @ViewChild('comboBox') box;

    public options = [];
    public selector = null;

    ngOnInit() {
        for(var cottage of this.cottages) {
            if (!this.options.some(x => x === cottage.Beds)) {
                this.options.push(cottage.Beds);
            }
        }
        this.options.sort((n1, n2) => n1 - n2);
        this.options.unshift("Any");
    }

    ngAfterViewInit() {
        this.selector = this.box.nativeElement;
    }
}
