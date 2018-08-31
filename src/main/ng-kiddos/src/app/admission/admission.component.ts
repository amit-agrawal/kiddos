import { Component, OnInit } from '@angular/core';
import { AdmissionService, Admission } from './admission.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admission',
  templateUrl: './admission.component.html',
  styleUrls: ['./admission.component.css']
})
export class AdmissionComponent implements OnInit {
  selectedAdmission : Admission;
  admissions : Admission[];
  admissionColumns: any[];
  constructor(private admissionService: AdmissionService, private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() 
  {
    this.admissions = this.admissionService.getAllAdmissions();
    this.admissionColumns = [
      {field:'firstName', header:'First Name'},
      {field:'lastName', header:'Last Name'},
      {field:'age', header:'Age'},
      {field:'termMonth', header:'Term Month'},
      {field:'program', header:'Program'},
      {field:'batchPlan', header:'Batch Plan'},
      {field:'admissionDate', header:'Admission Date'},              
    ];
    //console.log(this.admissions);
  }

  onNew()
  {
    this.router.navigate(['new'], {relativeTo: this.route});
  }

  onEdit()
  {
    this.router.navigate([this.selectedAdmission.id, 'edit'], {relativeTo: this.route});
  }
  onDelete()
  {
    // todo : add are you sure??
    this.admissionService.deleteAdmission(this.selectedAdmission.id);
  }
}
