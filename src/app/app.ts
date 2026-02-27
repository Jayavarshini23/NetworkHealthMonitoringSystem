import { Component, signal } from '@angular/core';

import { RouterOutlet, provideRouter } from '@angular/router';

import { routes } from './app.routes';



@Component({

  selector: 'app-root',

  imports: [RouterOutlet],

  templateUrl: './app.html',

  styleUrl: './app.scss'

})

export class App {

  protected readonly title = signal('network-health-dashboard');

}

