import {it, describe, expect, inject, beforeEachProviders} from 'angular2/testing';
import {CottageService} from "./cottages.service";

describe('CottageApi Service', () => {

    beforeEachProviders(() => [CottageService]);

    it('should ...', inject([CottageService], (service:CottageService) => {
        expect(service.url).toBe('/api/cottage');
    }));
});
