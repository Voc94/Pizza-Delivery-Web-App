import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StafforderComponent } from './stafforder.component';

describe('StafforderComponent', () => {
  let component: StafforderComponent;
  let fixture: ComponentFixture<StafforderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StafforderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StafforderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
