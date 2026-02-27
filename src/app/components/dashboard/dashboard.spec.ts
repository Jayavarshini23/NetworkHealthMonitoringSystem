import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { Dashboard } from './dashboard';
import { ApiService } from '../../services/api';

// Add jasmine import
declare const jasmine: any;

describe('Dashboard', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;

  // Mock ApiService with all methods used by Dashboard
  const mockApiService = {
    getDeviceCountByDomain: jasmine.createSpy('getDeviceCountByDomain').and.returnValue(of([
      { domain: 'ACCESS', count: 71 },
      { domain: 'CORE', count: 45 }
    ])),
    getDeviceStatusCounts: jasmine.createSpy('getDeviceStatusCounts').and.returnValue(of({
      active: 593,
      inactive: 0
    })),
    getNewlyAddedCount: jasmine.createSpy('getNewlyAddedCount').and.returnValue(of(15)),
    getNewlyAddedDevices: jasmine.createSpy('getNewlyAddedDevices').and.returnValue(of([])),
    getDevicesByDomain: jasmine.createSpy('getDevicesByDomain').and.returnValue(of([]))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        { provide: ApiService, useValue: mockApiService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Triggers ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load domain data on init', () => {
    expect(mockApiService.getDeviceCountByDomain).toHaveBeenCalled();
    expect(component.domainData.length).toBe(2);
  });

  it('should load status data on init', () => {
    expect(mockApiService.getDeviceStatusCounts).toHaveBeenCalled();
    expect(component.statusData?.active).toBe(593);
  });
});
