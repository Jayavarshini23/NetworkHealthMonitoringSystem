import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusWidget } from './status-widget';

describe('StatusWidget', () => {
  let component: StatusWidget;
  let fixture: ComponentFixture<StatusWidget>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatusWidget]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StatusWidget);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
