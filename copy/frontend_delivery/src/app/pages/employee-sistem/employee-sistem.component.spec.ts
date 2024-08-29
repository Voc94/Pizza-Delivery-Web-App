import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeSistemComponent } from './employee-sistem.component';

describe('EmployeeSistemComponent', () => {
  let component: EmployeeSistemComponent;
  let fixture: ComponentFixture<EmployeeSistemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EmployeeSistemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EmployeeSistemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
