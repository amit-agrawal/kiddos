import { NgModule } from '@angular/core';
import {Routes, RouterModule}  from '@angular/router';
import { PaymentComponent } from '../payment/payment.component';
import { ProgramComponent } from '../program/program.component';
import { AppComponent } from '../app.component';
import { StaffComponent } from '../staff/staff.component';
import { TransportComponent } from '../transport/transport.component';
import { ChildComponent } from '../child/child.component';
import { AdmissionComponent } from '../admission/admission.component';
import { HomeComponent } from '../home/home.component';
import { AdmissionEditComponent } from '../admission/admission-edit/admission-edit.component';


const appRoutes: Routes = [
  {path:'', component: HomeComponent},
  {path:'payment', component: PaymentComponent},
  {path:'program', component: ProgramComponent},
  {path:'staff', component: StaffComponent},
  {path:'transport', component: TransportComponent},
  {path:'child', component: ChildComponent},
  {path:'admission', component: AdmissionComponent},
  {path:'admission/new', component: AdmissionEditComponent},
  {path:'admission/:id/edit', component: AdmissionEditComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports:[RouterModule],
  declarations: []
})
export class AppRoutingModule { }
