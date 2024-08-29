import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientRestaurantDialogComponent } from './client-restaurant-dialog.component';

describe('ClientRestaurantDialogComponent', () => {
  let component: ClientRestaurantDialogComponent;
  let fixture: ComponentFixture<ClientRestaurantDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClientRestaurantDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ClientRestaurantDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
