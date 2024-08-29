import { TestBed } from '@angular/core/testing';

import { RestaurantmanagingserviceService } from '../../restaurantmanagingservice.service';

describe('RestaurantmanagingserviceService', () => {
  let service: RestaurantmanagingserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RestaurantmanagingserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
