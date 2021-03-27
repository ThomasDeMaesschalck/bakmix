import { Routes } from '@angular/router';
import {TracingViewComponent} from './tracing-view/tracing-view.component';

export const TRACING_ROUTES: Routes = [
  {
    path: 'tracing/:uniqueCode',
    component: TracingViewComponent
  }
];
