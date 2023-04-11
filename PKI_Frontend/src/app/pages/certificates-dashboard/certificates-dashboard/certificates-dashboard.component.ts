import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-certificates-dashboard',
  templateUrl: './certificates-dashboard.component.html',
  styleUrls: ['./certificates-dashboard.component.scss']
})
export class CertificatesDashboardComponent implements OnInit{
  
  private id: any;

  constructor(private _route: ActivatedRoute){
    this.id = this._route.snapshot.paramMap.get('id');
  }


  ngOnInit(): void {
  }

}
