import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminCertificatesComponent } from './pages/admin-certificates/admin-certificates.component';
import { LoginComponent } from './pages/login/login.component';
import { IntermediaryFormComponent } from './pages/intermediary-form/intermediary-form.component';
import { CertificatesDashboardComponent } from './pages/certificates-dashboard/certificates-dashboard/certificates-dashboard.component';

const routes: Routes = [
  { path: 'adminCertificates', component: AdminCertificatesComponent },
  { path: 'login', component: LoginComponent },
  { path: 'intermidiaryCertificate', component: IntermediaryFormComponent},
  { path: 'dashboard/:id', component: CertificatesDashboardComponent }
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
