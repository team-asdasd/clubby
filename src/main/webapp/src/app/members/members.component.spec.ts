import {
    it,
    describe,
    injectAsync,
    TestComponentBuilder,
    beforeEachProviders
} from 'angular2/testing';

import {Members} from './members.component';

describe('About Component', () => {

    beforeEachProviders(() => []);

    it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
        return tcb.createAsync(Members).then((fixture) => {
            fixture.detectChanges();
        });
    }));
});
