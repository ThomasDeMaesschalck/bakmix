import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderViewSendComponent } from './order-view-send.component';

describe('OrderViewSendComponent', () => {
  let component: OrderViewSendComponent;
  let fixture: ComponentFixture<OrderViewSendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderViewSendComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderViewSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
