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
import {AllReservations} from './all-reservations.component';

describe('Reservations Component', () => {

    beforeEachProviders((): any[] => []);

    it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
       return tcb.createAsync(AllReservations).then((fixture) => {
          fixture.detectChanges();
        });
    }));
});
