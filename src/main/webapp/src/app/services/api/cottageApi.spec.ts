import {
  it,
  describe,
  expect,
  inject,
  beforeEachProviders
} from 'angular2/testing';
import {CottageApi} from "./cottageApi";

describe('CottageApi Service', () => {

  beforeEachProviders(() => [CottageApi]);

  it('should ...', inject([CottageApi], (api:CottageApi) => {
    expect(api.url).toBe('/api/cottage');
  }));

});
