import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Device } from '../../services/api';

@Component({
  selector: 'app-device-table',
  imports: [CommonModule, FormsModule],
  templateUrl: './device-table.html',
  styleUrl: './device-table.scss'
})
export class DeviceTable implements OnInit {
  @Input() devices: Device[] = [];
  @Input() domain: string | null = null;
  @Input() showBackButton: boolean = false;
  @Output() backClick = new EventEmitter<void>();
  @Output() deviceClick = new EventEmitter<Device>();
  
  isLoading = false;
  error: string | null = null;
  searchTerm: string = '';
  filteredDevices: Device[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    if (!this.devices || this.devices.length === 0) {
      this.loadDevices();
    } else {
      this.filteredDevices = this.devices;
    }
  }

  loadDevices() {
    this.isLoading = true;
    this.error = null;
    
    const observable = this.domain 
      ? this.apiService.getDevicesByDomain(this.domain)
      : this.apiService.getDevices();

    observable.subscribe({
      next: (data) => {
        this.devices = data;
        this.filteredDevices = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load devices';
        this.isLoading = false;
        console.error('Device table error:', err);
      }
    });
  }

  onSearchChange() {
    if (!this.searchTerm) {
      this.filteredDevices = this.devices;
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredDevices = this.devices.filter(device => 
        device.deviceName.toLowerCase().includes(term) ||
        device.deviceUid.toLowerCase().includes(term) ||
        device.domainName.toLowerCase().includes(term) ||
        device.currentStatus.toLowerCase().includes(term)
      );
    }
  }

  onDeviceClick(device: Device) {
    this.deviceClick.emit(device);
  }

  onBackClick() {
    this.backClick.emit();
  }

  getStatusColor(status: string): string {
    return status === 'ACTIVE' ? '#00ff41' : '#ff4444';
  }

  getDeviceCount(): number {
    return this.filteredDevices.length;
  }

  getActiveCount(): number {
    return this.filteredDevices.filter(d => d.currentStatus === 'ACTIVE').length;
  }

  getInactiveCount(): number {
    return this.filteredDevices.filter(d => d.currentStatus === 'INACTIVE').length;
  }
}
