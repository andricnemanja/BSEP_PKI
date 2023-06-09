import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CredentialsInterceptor} from './helpers/jwt.interceptor.service'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AdminCertificatesComponent } from './pages/admin-certificates/admin-certificates.component'
import { LoginComponent } from './pages/login/login.component';
import { IntermediaryFormComponent } from './pages/intermediary-form/intermediary-form.component';
import { CertificatesDashboardComponent } from './pages/certificates-dashboard/certificates-dashboard/certificates-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminCertificatesComponent,
    LoginComponent,
    IntermediaryFormComponent,
    CertificatesDashboardComponent
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CredentialsInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
