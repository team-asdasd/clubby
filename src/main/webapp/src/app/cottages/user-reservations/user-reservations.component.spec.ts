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
import {UserReservations} from './user-reservations.component';

describe('Reservations Component', () => {

    beforeEachProviders((): any[] => []);

    it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
       return tcb.createAsync(UserReservations).then((fixture) => {
          fixture.detectChanges();
        });
    }));
});
