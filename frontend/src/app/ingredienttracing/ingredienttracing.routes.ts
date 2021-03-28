import { Routes } from '@angular/router';
import {TracingViewComponent} from './tracing-view/tracing-view.component';
import {TracingMailComponent} from './tracing-mail/tracing-mail.component';

export const TRACING_ROUTES: Routes = [
  {
    path: 'tracing/:uniqueCode',
    component: TracingViewComponent
  },
  {
    path: 'tracing/:uniqueCode/mail',
    component: TracingMailComponent
  }
];
