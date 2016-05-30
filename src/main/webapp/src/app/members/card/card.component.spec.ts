import {
    it,
    describe,
    injectAsync,
    TestComponentBuilder,
    beforeEachProviders
} from 'angular2/testing';

import {Card} from './card.component';

describe('About Component', () => {

    beforeEachProviders(() => []);

    it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
        return tcb.createAsync(Card).then((fixture) => {
            //fixture.detectChanges();
        });
    }));
});
