import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { AdmissionService, Admission } from '../admission.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SrdService, SrdData } from '../../shared/srd.service';

@Component({
  selector: 'app-admission-edit',
  templateUrl: './admission-edit.component.html',
  styleUrls: ['./admission-edit.component.css']
})
export class AdmissionEditComponent implements OnInit {
  id : number;
  editMode = false;
  selectedAdmission : Admission;
  admissionForm : FormGroup;
  programs : SrdData[];

  constructor(private route: ActivatedRoute, 
    private router: Router, 
    private admissionService:AdmissionService,
    private srdService : SrdService) { }

  ngOnInit() {
    
    this.programs = this.srdService.getPrograms();
    console.log(this.programs);
    this.route.params.subscribe((params: Params)=>{
      this.id = +params['id'];
      this.editMode = params['id'] != null;
      this.initForm();
    });
  }

  initForm()
  {
    
    if(this.editMode)
    {
        this.selectedAdmission =  this.admissionService.getAdmissionById(this.id);
            console.log(this.selectedAdmission);
    }
    else{
      this.selectedAdmission = new Admission(-1,'','','','',null,'','');
    }
    this.admissionForm = new FormGroup(
      {
        'firstName': new FormControl(this.selectedAdmission.firstName, Validators.required),
        'lastName': new FormControl(this.selectedAdmission.lastName, Validators.required),
        'age': new FormControl(this.selectedAdmission.age, Validators.required),
        'termMonth': new FormControl(this.selectedAdmission.termMonth, Validators.required),
        'program': new FormControl(this.selectedAdmission.program, Validators.required),
        'batchPlan': new FormControl(this.selectedAdmission.batchPlan, Validators.required),
        'admissionDate': new FormControl(this.selectedAdmission.admissionDate, Validators.required),
      }
    );
  }

  onSave()
  {
    let updateAdmission = new Admission(this.selectedAdmission.id, 
      this.admissionForm.value['firstName'],
      this.admissionForm.value['lastName'],
      this.admissionForm.value['age'],
      this.admissionForm.value['termMonth'],
      this.admissionForm.value['program'],
      this.admissionForm.value['batchPlan'],
      this.admissionForm.value['admissionDate']);
      if(this.editMode)
      {
        this.admissionService.updateAdmission(this.selectedAdmission.id, updateAdmission);
      }
      else{
        this.admissionService.addAdmission(updateAdmission);
      }
        this.router.navigate(['/admission'], {relativeTo: this.route});
      }
}
