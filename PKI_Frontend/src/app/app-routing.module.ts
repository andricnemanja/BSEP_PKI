import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminCertificatesComponent } from './pages/admin-certificates/admin-certificates.component';
import { LoginComponent } from './pages/login/login.component';

const routes: Routes = [
  { path: 'adminCertificates', component: AdminCertificatesComponent },
  { path: 'login', component: LoginComponent },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
