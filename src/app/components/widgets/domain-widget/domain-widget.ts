import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, DeviceCountByDomain } from '../../../services/api';

@Component({
  selector: 'app-domain-widget',
  imports: [CommonModule],
  templateUrl: './domain-widget.html',
  styleUrl: './domain-widget.scss'
})
export class DomainWidget implements OnInit {
  @Input() domainData: DeviceCountByDomain[] = [];
  @Output() domainClick = new EventEmitter<string>();
  isLoading = false;
  error: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    if (!this.domainData || this.domainData.length === 0) {
      this.loadDomainData();
    }
  }

  loadDomainData() {
    this.isLoading = true;
    this.error = null;
    
    this.apiService.getDeviceCountByDomain().subscribe({
      next: (data) => {
        this.domainData = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load domain data';
        this.isLoading = false;
        console.error('Domain widget error:', err);
      }
    });
  }

  onDomainClick(domain: string) {
    this.domainClick.emit(domain);
  }

  getTotalDevices(): number {
    return this.domainData.reduce((sum, item) => sum + item.count, 0);
  }
}
