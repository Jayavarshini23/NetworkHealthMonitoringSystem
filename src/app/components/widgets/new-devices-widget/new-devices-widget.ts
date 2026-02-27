import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, Device } from '../../../services/api';

@Component({
  selector: 'app-new-devices-widget',
  imports: [CommonModule],
  templateUrl: './new-devices-widget.html',
  styleUrl: './new-devices-widget.scss'
})
export class NewDevicesWidget implements OnInit {
  @Input() devices: Device[] = [];
  @Input() hours: number = 24;
  isLoading = false;
  error: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    if (!this.devices || this.devices.length === 0) {
      this.loadNewDevices();
    }
  }

  loadNewDevices() {
    this.isLoading = true;
    this.error = null;
    
    this.apiService.getDevicesByAge(this.hours).subscribe({
      next: (data) => {
        this.devices = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load new devices';
        this.isLoading = false;
        console.error('New devices widget error:', err);
      }
    });
  }

  getTimeAgo(createdAt: string): string {
    const created = new Date(createdAt);
    const now = new Date();
    const diffMs = now.getTime() - created.getTime();
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffMins = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));

    if (diffHours > 0) {
      return `${diffHours}h ${diffMins}m ago`;
    } else {
      return `${diffMins}m ago`;
    }
  }

  getStatusColor(status: string): string {
    return status === 'ACTIVE' ? '#00ff41' : '#ff4444';
  }
}
