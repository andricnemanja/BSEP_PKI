import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Certificate } from '../model/certificate';

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {

  apiHost: string = 'http://localhost:8080/';
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  getAllCertificates(): Observable<Certificate[]> {
    return this.http.get<[]>(this.apiHost + 'CertificateController/getAll' , {headers: this.headers});
  }

  getAllEquipment(userEmail: string): Observable<Certificate[]> {
    return this.http.get<Certificate[]>(this.apiHost + 'CertificateController/getAll/' + userEmail, {headers: this.headers});
  }

}
