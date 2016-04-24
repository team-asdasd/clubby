import {
  it,
  describe,
  expect,
  inject,
  beforeEachProviders
} from 'angular2/testing';
import {UserApi} from './userApi';

describe('UserApi Service', () => {

  beforeEachProviders(() => [UserApi]);

  it('should ...', inject([UserApi], (api:UserApi) => {
    expect(api.url).toBe('/api');
  }));

});
