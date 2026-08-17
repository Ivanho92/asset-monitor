import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: `
    <div class="wrapper">
      <h1>{{ title() }}</h1>
      <router-outlet />
    </div>
  `,
})
export class App {
  protected readonly title = signal('Asset Monitor');
}
