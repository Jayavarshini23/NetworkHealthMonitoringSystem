import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomainWidget } from './domain-widget';

describe('DomainWidget', () => {
  let component: DomainWidget;
  let fixture: ComponentFixture<DomainWidget>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DomainWidget]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DomainWidget);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
