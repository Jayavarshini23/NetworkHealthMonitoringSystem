import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDevicesWidget } from './new-devices-widget';

describe('NewDevicesWidget', () => {
  let component: NewDevicesWidget;
  let fixture: ComponentFixture<NewDevicesWidget>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewDevicesWidget]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewDevicesWidget);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
