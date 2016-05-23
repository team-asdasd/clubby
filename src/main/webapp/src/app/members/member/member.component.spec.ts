import {
    it,
    describe,
    injectAsync,
    TestComponentBuilder,
    beforeEachProviders
} from 'angular2/testing';

import {Member} from './member.component';

describe('About Component', () => {

    beforeEachProviders(() => []);

    it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
        return tcb.createAsync(Member).then((fixture) => {
            fixture.detectChanges();
        });
    }));
});
