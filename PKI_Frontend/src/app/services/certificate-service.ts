import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Certificate } from '../model/certificate';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  apiHost: string = 'http://localhost:8080/';
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  getAllCertificates(): Observable<Certificate[]> {
    return this.http.get<[]>(this.apiHost + 'CertificateController/getAll' , {headers: this.headers});
  }

  getCertificatesByUserEmail(userEmail: string): Observable<any[]> {
    return this.http.get<any[]>(this.apiHost + 'CertificateController/getBySubjectEmail/email?email=' + userEmail, {headers: this.headers});
  }

}
