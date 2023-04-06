import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminCertificatesComponent } from './pages/admin-certificates/admin-certificates.component';

const routes: Routes = [
  { path: 'adminCertificates', component: AdminCertificatesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
