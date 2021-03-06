import {
    it,
    describe,
    injectAsync,
    TestComponentBuilder,
    beforeEachProviders
} from 'angular2/testing';

import {Form} from './form.component';

describe('About Component', () => {

    beforeEachProviders(() => []);

    it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
        return tcb.createAsync(Form).then((fixture) => {
            fixture.detectChanges();
        });
    }));
});
