import {
  it,
  describe,
  expect,
  inject,
  beforeEachProviders
} from 'angular2/testing';
import {UserApi} from './user.service.ts';

describe('UserApi Service', () => {

  beforeEachProviders(() => [UserApi]);

  it('should ...', inject([UserApi], (api:UserApi) => {
    expect(api.url).toBe('/api/user');
  }));

});
