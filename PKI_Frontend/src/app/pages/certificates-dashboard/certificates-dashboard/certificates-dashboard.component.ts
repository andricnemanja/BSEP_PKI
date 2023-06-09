import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CertificateService } from 'src/app/services/certificate-service';

@Component({
  selector: 'app-certificates-dashboard',
  templateUrl: './certificates-dashboard.component.html',
  styleUrls: ['./certificates-dashboard.component.scss']
})
export class CertificatesDashboardComponent implements OnInit{
  
  private id: any;
  certificates : any[] = [];

  constructor(private _route: ActivatedRoute, private certificateService: CertificateService, private http: HttpClient){
    this.id = this._route.snapshot.paramMap.get('id');
  }


  ngOnInit(): void {
    this.certificateService.getCertificatesByUserEmail(this.id).subscribe(res => {
      this.certificates = res;
    });
  }

  public downloadCertificate(alias: string): void {
    const options = {
        headers: new HttpHeaders({
            'Content-Type': 'application/json',
        }),
        responseType: 'arraybuffer' as 'json' // or 'text'
    };
    this.http.get('http://localhost:8080/CertificateController/export/' + alias, options).subscribe(
        (response) => {
            const blob = new Blob([response as ArrayBuffer], { type: 'application/x-x509-ca-cert' }); // create blob object with file content and content type
            const link = document.createElement('a'); // create a element to download the file
            link.href = window.URL.createObjectURL(blob);
            link.download = `${alias}.cer`;
            link.click();
        },
        (error) => console.log('Error exporting certificate:', error)
    );
  }


  public Revoke(serialNumber:any){
    this.certificateService.getRevokedCertificates(serialNumber).subscribe(res => {
    });;
    
  }



}
