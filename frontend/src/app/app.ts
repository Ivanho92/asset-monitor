import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: `
    <h1>{{ title() }}</h1>
    <p>Frontend skeleton is running. Live feed table comes in a later step.</p>

    <router-outlet>
  `,
  styles: [],
})
export class App {
  protected readonly title = signal('Asset Monitor');
}
