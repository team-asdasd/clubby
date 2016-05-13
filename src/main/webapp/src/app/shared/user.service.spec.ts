import {
  it,
  describe,
  expect,
  inject,
  beforeEachProviders
} from 'angular2/testing';
import {UserService} from './user.service.ts';

describe('UserApi Service', () => {

  beforeEachProviders(() => [UserService]);

  it('should ...', inject([UserService], (api:UserService) => {
    expect(api.url).toBe('/api/user');
  }));

});
