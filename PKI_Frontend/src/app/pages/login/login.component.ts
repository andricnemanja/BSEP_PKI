import { CredentialsDTO } from './../../DTOs/credentialsDTO';
import { Component, NgModule, OnInit } from '@angular/core';
import { networkInterfaces } from 'os';
import { CertificateParamsDTO } from 'src/app/DTOs/certificateParamsDTO';
import { HttpClient, HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { runInThisContext } from 'vm';
import { Observable } from 'rxjs';
import { LoginDTO } from 'src/app/DTOs/loginDTO';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {
  public loginData : LoginDTO = new LoginDTO();
  public credentialsDTO : CredentialsDTO = new CredentialsDTO();

  constructor(private http: HttpClient) {}

  ngOnInit(){

  }

  onLogin() {
    this.login(this.loginData).subscribe(res => {
      this.credentialsDTO = res;

      localStorage.setItem('UserID', this.credentialsDTO.userID.toString());
      localStorage.setItem('email', this.credentialsDTO.email.toString());
    });
  }
  onLogout() {
    this.logout().subscribe(res => {

      localStorage.removeItem('UserID');
      localStorage.removeItem('email')
    });
  }

  // POZIVI FUNKCIJA U NOVOM FAJLU, ZA SAD NEK BUDU OVDE >>>
  login(loginData : LoginDTO) : Observable<any> {
    
    return this.http.post<any>("http://localhost:8080/UserController/login", this.loginData)
  }
  logout() : Observable<any> {

    return this.http.post<any>("http://localhost:8080/UserController/logout", this.loginData)
  }
}
