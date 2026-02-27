import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomainWidget } from '../widgets/domain-widget/domain-widget';
import { StatusWidget } from '../widgets/status-widget/status-widget';
import { NewDevicesWidget } from '../widgets/new-devices-widget/new-devices-widget';
import { DeviceTable } from '../device-table/device-table';
import { ApiService, Device, DeviceCountByDomain, DeviceStatusCount } from '../../services/api';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, DomainWidget, StatusWidget, NewDevicesWidget, DeviceTable],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  showDeviceTable = false;
  selectedDomain: string | null = null;
  domainData: DeviceCountByDomain[] = [];
  statusData: DeviceStatusCount | null = null;
  newDevices: Device[] = [];
  newDevicesCount: number = 0;
  devicesForTable: Device[] = [];
  allDevicesByDomain: Map<string, Device[]> = new Map(); // Store all devices by domain
  refreshInterval: any;
  isLoading = false;
  error: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    console.log('Dashboard initialized');
    // Load domain stats automatically
    this.loadDomainStats();
    // Then start auto-refresh for continuous updates
    this.startAutoRefresh();
  }

  ngOnDestroy() {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  }

  startAutoRefresh() {
    // Refresh data every 5 seconds
    this.refreshInterval = setInterval(() => {
      this.refreshData();
    }, 5000);
  }

  refreshData() {
    // Load real data from backend
    this.loadDomainData();
    this.loadStatusData();
    this.loadNewDevices();
    // Load devices for all domains automatically
    this.loadAllDevicesByDomain();
  }

  loadDomainData() {
    console.log('Loading domain data...');
    this.apiService.getDeviceCountByDomain().subscribe({
      next: (data) => {
        console.log('Domain data received:', data);
        this.domainData = data;
      },
      error: (error) => {
        console.error('Failed to load domain data:', error);
      }
    });
  }

  loadStatusData() {
    this.apiService.getDeviceStatusCounts().subscribe({
      next: (data) => {
        this.statusData = data;
      },
      error: (error) => {
        console.error('Failed to load status data:', error);
      }
    });
  }

  loadNewDevices() {
    this.apiService.getNewlyAddedCount().subscribe({
      next: (count) => {
        console.log('New devices count received:', count);
        // Store count instead of array for display
        this.newDevicesCount = count;
      },
      error: (error) => {
        console.error('Failed to load new devices count:', error);
      }
    });
  }

  loadNewDevicesList() {
    this.apiService.getNewlyAddedDevices().subscribe({
      next: (devices) => {
        console.log('New devices list received:', devices);
        this.newDevices = devices;
      },
      error: (error) => {
        console.error('Failed to load new devices list:', error);
      }
    });
  }

  loadDomainStats() {
    this.isLoading = true;
    this.apiService.getDeviceCountByDomain().subscribe({
      next: (data) => {
        console.log("Domain stats loaded:", data);
        this.domainData = data;
        this.isLoading = false;
        
        // Auto-load first domain devices
        if (this.domainData.length > 0) {
          const firstDomain = this.domainData[0].domain;
          this.selectedDomain = firstDomain;
          this.showDeviceTable = true;
          this.loadDevicesForDomain(firstDomain);
        }
      },
      error: (err) => {
        console.error("Failed to load domain stats", err);
        this.isLoading = false;
        this.error = "Failed to load domain stats";
      }
    });
  }

  loadAllDevicesByDomain() {
    // First load domain data to get all domains
    this.apiService.getDeviceCountByDomain().subscribe({
      next: (domains) => {
        console.log('Loading devices for all domains:', domains);
        // For each domain, load its devices
        domains.forEach(domain => {
          this.loadDevicesForDomain(domain.domain);
        });
      },
      error: (error) => {
        console.error('Failed to load domains for device loading:', error);
      }
    });
  }

  onDomainClick(domain: string) {
    this.selectedDomain = domain;
    this.showDeviceTable = true;
    this.loadDevicesForDomain(domain);
  }

  loadDevicesForDomain(domain: string) {
    console.log('Domain sent to API:', domain);
    console.log('Domain type:', typeof domain);
    console.log('Domain value:', JSON.stringify(domain));
    
    this.apiService.getDevicesByDomain(domain).subscribe({
      next: (devices) => {
        console.log(`Devices for ${domain}:`, devices);
        // Store devices for the table
        this.devicesForTable = devices;
        // Store devices in the map for all domains
        this.allDevicesByDomain.set(domain, devices);
      },
      error: (error) => {
        console.error(`Failed to load devices for ${domain}:`, error);
      }
    });
  }

  onBackToDashboard() {
    this.showDeviceTable = false;
    this.selectedDomain = null;
  }

  onDeviceClick(device: Device) {
    console.log('Device clicked:', device);
    // TODO: Show device details modal or navigate to device details page
  }

  // Helper methods for template
  getTotalDevices(): number {
    return this.domainData.reduce((sum, item) => sum + item.count, 0);
  }

  getActiveRate(): number {
    if (!this.statusData) return 0;
    const total = this.statusData.active + this.statusData.inactive;
    return total > 0 ? (this.statusData.active / total) * 100 : 0;
  }

  getNewDevicesCount(): number {
    return this.newDevices.length;
  }

  getDomainsCount(): number {
    return this.domainData.length;
  }
}
