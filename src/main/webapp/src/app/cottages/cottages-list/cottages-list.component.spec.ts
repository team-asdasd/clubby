import {
    it,
    iit,
    describe,
    ddescribe,
    expect,
    inject,
    injectAsync,
    TestComponentBuilder,
    beforeEachProviders
} from 'angular2/testing';
import {provide} from 'angular2/core';
import {CottagesList} from './cottages-list.component';

describe('Cottages-list Component', () => {

    beforeEachProviders((): any[] => []);

    it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
        return tcb.createAsync(CottagesList).then((fixture) => {
            fixture.detectChanges();
        });
    }));
});
