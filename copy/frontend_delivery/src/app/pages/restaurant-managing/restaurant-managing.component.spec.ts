import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestaurantManagingComponent } from './restaurant-managing.component';

describe('RestaurantManagingComponent', () => {
  let component: RestaurantManagingComponent;
  let fixture: ComponentFixture<RestaurantManagingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RestaurantManagingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RestaurantManagingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
