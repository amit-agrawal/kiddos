import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { PaymentComponent } from './payment/payment.component';
import { ProgramComponent } from './program/program.component';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing/app-routing.module';
import { StaffComponent } from './staff/staff.component';
import { TransportComponent } from './transport/transport.component';
import { AdmissionComponent } from './admission/admission.component';
import { ChildComponent } from './child/child.component';
import { HomeComponent } from './home/home.component';
import { AdmissionService } from './admission/admission.service';
import { TableModule } from 'primeng/table';

@NgModule({
  declarations: [
    AppComponent,
    PaymentComponent,
    ProgramComponent,
    StaffComponent,
    TransportComponent,
    AdmissionComponent,
    ChildComponent,
    HomeComponent   
  ],
  imports: [
    BrowserModule ,
    AppRoutingModule,
    TableModule
    
  ],
  providers: [AdmissionService],
  bootstrap: [AppComponent],
  schemas:[CUSTOM_ELEMENTS_SCHEMA ]
})
export class AppModule { }
