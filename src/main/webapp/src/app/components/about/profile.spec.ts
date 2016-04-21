import {
  it,
  describe,
  injectAsync,
  TestComponentBuilder,
  beforeEachProviders
} from 'angular2/testing';

import {Profile} from './profile';

describe('About Component', () => {

  beforeEachProviders(() => []);

  it('should ...', injectAsync([TestComponentBuilder], (tcb:TestComponentBuilder) => {
    return tcb.createAsync(Profile).then((fixture) => {
      fixture.detectChanges();
    });
  }));

});
