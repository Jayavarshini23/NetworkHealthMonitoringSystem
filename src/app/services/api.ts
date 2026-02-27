import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface Device {
  id?: number;
  deviceUid: string;
  deviceName: string;
  domainName: string;
  currentStatus: 'ACTIVE' | 'INACTIVE';
  lastSeenAt: string;
  createdAt: string;
}

export interface HealthMetric {
  id?: number;
  deviceUid: string;
  cpuUsage: number;
  memoryUsage: number;
  lastSeenTs: string;
}

export interface DeviceCountByDomain {
  domain: string;  // Fixed: backend returns 'domain' not 'domainName'
  count: number;
}

export interface DeviceStatusCount {
  active: number;
  inactive: number;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8085/api'; // Backend device-service URL

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }

  // Get all devices - Fixed to match backend DeviceController
  getDevices(): Observable<Device[]> {
    console.log(`Calling API: ${this.baseUrl}/devices?limit=1000`);
    return this.http.get<Device[]>(`${this.baseUrl}/devices?limit=1000`, { headers: this.getHeaders() });
  }

  // Get devices by domain - Clean professional version using params
  getDevicesByDomain(domain: string): Observable<Device[]> {
    console.log(`Calling: http://localhost:8085/api/devices/devicesbydomain?domain=${domain}`);
    console.log('Domain sent:', domain);
    
    return this.http.get<Device[]>(
      `http://localhost:8085/api/devices/devicesbydomain`,
      { 
        params: { domain },
        headers: this.getHeaders()
      }
    );
  }

  // Get device count by domain - Fixed to match exact URL
  getDeviceCountByDomain(): Observable<DeviceCountByDomain[]> {
    console.log(`Calling API: ${this.baseUrl}/domains/device-count`);
    return this.http.get<DeviceCountByDomain[]>(`${this.baseUrl}/domains/device-count`, { headers: this.getHeaders() });
  }

  // Get device status counts - Fixed to match backend StatsController  
  getDeviceStatusCounts(): Observable<DeviceStatusCount> {
    console.log(`Calling API: ${this.baseUrl}/stats/active-vs-inactive`);
    return this.http.get<DeviceStatusCount>(`${this.baseUrl}/stats/active-vs-inactive`, { headers: this.getHeaders() });
  }

  // Get newly added devices count - Fixed to match backend StatsController
  getNewlyAddedCount(hours: number = 24): Observable<number> {
    const url = `${this.baseUrl}/stats/newly-added?hours=${hours}`;
    console.log(`Calling API: ${url}`);
    return this.http.get<number>(url, { headers: this.getHeaders() });
  }

  // Get newly added devices list - Fixed to match backend DeviceController
  getNewlyAddedDevices(): Observable<Device[]> {
    console.log(`Calling API: ${this.baseUrl}/devices/devices/recent?hours=24`);
    return this.http.get<Device[]>(`${this.baseUrl}/devices/devices/recent?hours=24`, { headers: this.getHeaders() });
  }

  // Get devices by age - Fixed to match backend DeviceController
  getDevicesByAge(hours: number): Observable<Device[]> {
    console.log(`Calling API: ${this.baseUrl}/devices/devices/recent?hours=${hours}`);
    return this.http.get<Device[]>(`${this.baseUrl}/devices/devices/recent?hours=${hours}`, { headers: this.getHeaders() });
  }

  // Get device by ID - Fixed to match backend DeviceController (uses ID not deviceUid)
  getDeviceByUid(deviceUid: string): Observable<Device> {
    console.log(`Calling API: ${this.baseUrl}/devices/${deviceUid}`);
    return this.http.get<Device>(`${this.baseUrl}/devices/${deviceUid}`, { headers: this.getHeaders() });
  }

  // Create test device - Fixed to match backend DeviceController
  createTestDevice(device: { deviceUid: string; deviceName: string; domainName: string }): Observable<Device> {
    console.log(`Calling API: ${this.baseUrl}/devices/test-device`);
    return this.http.post<Device>(`${this.baseUrl}/devices/test-device`, device, { headers: this.getHeaders() });
  }
}
