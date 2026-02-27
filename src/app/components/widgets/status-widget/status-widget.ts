import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, DeviceStatusCount } from '../../../services/api';

@Component({
  selector: 'app-status-widget',
  imports: [CommonModule],
  templateUrl: './status-widget.html',
  styleUrl: './status-widget.scss'
})
export class StatusWidget implements OnInit {
  @Input() statusData: DeviceStatusCount | null = null;
  isLoading = false;
  error: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    if (!this.statusData) {
      this.loadStatusData();
    }
  }

  loadStatusData() {
    this.isLoading = true;
    this.error = null;
    
    this.apiService.getDeviceStatusCounts().subscribe({
      next: (data) => {
        this.statusData = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load status data';
        this.isLoading = false;
        console.error('Status widget error:', err);
      }
    });
  }

  getTotalDevices(): number {
    if (!this.statusData) return 0;
    return this.statusData.active + this.statusData.inactive;
  }

  getActivePercentage(): number {
    if (!this.statusData) return 0;
    const total = this.getTotalDevices();
    return total > 0 ? (this.statusData.active / total) * 100 : 0;
  }

  getInactivePercentage(): number {
    if (!this.statusData) return 0;
    const total = this.getTotalDevices();
    return total > 0 ? (this.statusData.inactive / total) * 100 : 0;
  }
}
