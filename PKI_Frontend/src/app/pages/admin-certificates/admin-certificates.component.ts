import { Component, NgModule, OnInit } from '@angular/core';
import { networkInterfaces } from 'os';
import { CertificateParamsDTO } from 'src/app/DTOs/certificateParamsDTO';
import { HttpClient, HttpErrorResponse, HttpParams, HttpStatusCode } from '@angular/common/http';
import { runInThisContext } from 'vm';
import { Observable, Subject } from 'rxjs';
import { CertificateInfoDTO } from 'src/app/DTOs/certificateInfoDTO';

@Component({
  selector: 'app-admin-certificates',
  templateUrl: './admin-certificates.component.html',
  styleUrls: ['./admin-certificates.component.scss']
})

export class AdminCertificatesComponent implements OnInit{
  public certificateType : String = "";
  public notBefore: Date = new Date();
  public issuer : String = "";
  public keyUsage : Array<String> = [];
  public extendedKeyUsage: Array<String> = [];
  public commonName : String = "";
  public surname : String = "";
  public givenName : String = "";
  public organization : String = "";
  public organizationUnit : String = "";
  public country : String = "";
  public email : String = "";
  public password : String = "";

  public digitalSignature : boolean = false;
  public nonRepudiation : boolean = false;
  public codeSigning : boolean = false;
  public emailProtection : boolean = false;
  public startDate : String = "";
  public startTime : String = "";

  public certificateParams : CertificateParamsDTO = new CertificateParamsDTO();

  public certificateInfoDTOs : Array<CertificateInfoDTO> = [];
  public selectedSerialNumber : String = "";

  constructor(private http: HttpClient) {}
  
  calculateDate() {
    const start = this.startDate + "T" + this.startTime;
    
    this.notBefore = new Date(start);
  }

  checkboxValues() {
    this.keyUsage = [];
    this.extendedKeyUsage = [];
    const digitalSignature = (this.digitalSignature) ? "digitalSignature" : "";
    const nonRepudiation = (this.nonRepudiation) ? "nonRepudiation" : "";
    this.keyUsage.push(digitalSignature, nonRepudiation);

    const codeSigning = (this.codeSigning) ? "codeSigning" : "";
    const emailProtection = (this.emailProtection) ? "emailProtection" : "";
    this.extendedKeyUsage.push(codeSigning, emailProtection);
  }

  onSubmit() {
    this.calculateDate();
    this.checkboxValues();

    this.certificateParams = {
      certificateType : this.certificateType,
      notBefore : this.notBefore,
      issuer : this.selectedSerialNumber,
      keyUsage : this.keyUsage,
      extendedKeyUsage : this.extendedKeyUsage,
      commonName : this.commonName,
      surname : this.surname,
      givenName : this.givenName,
      organization : this.organization,
      organizationUnit : this.organizationUnit,
      country : this.country,
      email : this.email,
      password : this.password
    }
    
    this.sendCertificateParams(this.certificateParams).subscribe(res => {

    });
    this.resetInputs();

  }

  sendCertificateParams(certificateParams : CertificateParamsDTO) : Observable<any> {
    return this.http.post<any>("http://localhost:8080/CertificateController/generateCertificate", certificateParams)
  }

  getAllCertificates(){
    return this.http.get<any>("http://localhost:8080/CertificateController/getBySubjectEmail/getAll")
  }


  resetInputs() {
    this.certificateType = "";
    this.notBefore = new Date();
    this.issuer = "";
    this.keyUsage = [];
    this.extendedKeyUsage = [];
    this.commonName = "";
    this.surname = "";
    this.givenName = "";
    this.organization = "";
    this.organizationUnit = "";
    this.country = "";
    this.email = "";
    this.password = "";
    this.digitalSignature = false;
    this.nonRepudiation = false;
    this.codeSigning = false;
    this.emailProtection = false;
  }

  ngOnInit(){
    this.getAllCertificates().subscribe(res => {
      this.certificateInfoDTOs = res;
    });
  }

}
