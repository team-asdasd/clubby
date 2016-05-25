import {it, describe, expect, inject, beforeEachProviders} from 'angular2/testing';
import {RecommendationService} from "./recommendation.service.ts";

describe('RecommendationAPI Service', () => {

    beforeEachProviders(() => [RecommendationService]);

    it('should ...', inject([RecommendationService], (service:RecommendationService) => {
        expect(service.receivedUrl).toBe('/api/recommendation/received');
    }));
});
