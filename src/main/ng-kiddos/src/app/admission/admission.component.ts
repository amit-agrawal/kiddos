import { Component, OnInit } from '@angular/core';
import { AdmissionService, Admission } from './admission.service';
import {TableModule} from 'primeng/table';
@Component({
  selector: 'app-admission',
  templateUrl: './admission.component.html',
  styleUrls: ['./admission.component.css']
})
export class AdmissionComponent implements OnInit {

  admissions : Admission[];
  constructor(private admissionService: AdmissionService) { }

  ngOnInit() 
  {
    this.admissions = this.admissionService.getAllAdmissions();
    //console.log(this.admissions);
  }
}
