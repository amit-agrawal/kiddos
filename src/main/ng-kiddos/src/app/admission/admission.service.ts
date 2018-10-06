import { SrdData, SrdService } from "../shared/srd.service";
import { Injectable } from "@angular/core";
//import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Headers, Http, Response } from '@angular/http';
import { Observable, of } from "rxjs";
import { catchError, map, tap } from 'rxjs/operators';

export class Admission{
    id:number;
    firstName:string;
    lastName:string;
    age:string;
    termMonth: string;
    program: SrdData;
    batchPlan: string;
    admissionDate: string

    constructor(
        id: number,
        firstName:string,
        lastName:string,
        age:string,
        termMonth: string,
        program: SrdData,
        batchPlan: string,
        admissionDate: string)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.termMonth = termMonth;
        this.program = program;
        this.batchPlan = batchPlan;
        this.admissionDate = admissionDate;
    }
}

@Injectable()
export class AdmissionService{

    constructor(private srdService: SrdService, 
        private http: Http){}

    admissions: Admission[] = [
    
        new Admission(1,'Sid',
            'Sharma',
            '3 years 2 months',
            'March 18',
            this.srdService.getPrograms()[1],
            'Morning',
            '2-2-2018'),
         new Admission(2, 'Adwika',
            'Chauvan',
            '4 years 1 months',
            'March 17',
            this.srdService.getPrograms()[0],
            'Evening',
            '2-2-2017'),
          new Admission(3, 'Myra',
            'Deshpande',
            '4 years 6 months',
            'March 16',
            this.srdService.getPrograms()[2],
            'Morning',
            '2-2-2016'),
            new Admission(3, 'Myra',
              'Deshpande',
              '4 years 6 months',
              'March 16',
              this.srdService.getPrograms()[2],
              'Morning',
              '2-2-2016'),
              new Admission(3, 'Myra',
                'Deshpande',
                '4 years 6 months',
                'March 16',
                this.srdService.getPrograms()[2],
                'Morning',
                '2-2-2016'),
                new Admission(3, 'Myra',
                  'Deshpande',
                  '4 years 6 months',
                  'March 16',
                this.srdService.getPrograms()[2],
                  'Morning',
                  '2-2-2016'),
                  new Admission(3, 'Myra',
                    'Deshpande',
                    '4 years 6 months',
                    'March 16',
                this.srdService.getPrograms()[2],
                    'Morning',
                    '2-2-2016'),
                    new Admission(3, 'Myra',
                      'Deshpande',
                      '4 years 6 months',
                      'March 16',
                this.srdService.getPrograms()[2],
                      'Morning',
                      '2-2-2016')
    ];
    
    getAllAdmissions() : Observable<Admission[]>
    {        
        var admissionUrl = 'http://localhost:8081/listAdmissions'; 
        console.log('before call');
        return this.http.get(admissionUrl)
        .pipe(map(response => <Admission[]>response.json().admission));                
    }

    private handleError<T> (operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
       
          // TODO: send the error to remote logging infrastructure
          console.error(error); // log to console instead
       
          // Let the app keep running by returning an empty result.
          return of(result as T);
        };
      }

    getAdmissionById(id: number)
    {
        return this.admissions.find(x => x.id == id);
    }

    deleteAdmission(id: number)
    {
        var index = this.admissions.findIndex(x => x.id == id);
        this.admissions.splice(index, 1);
    }

    updateAdmission(id: number, updatedAdmission: Admission)
    {
        console.log(updatedAdmission);
        var index = this.admissions.findIndex(x => x.id == id);
        this.admissions[index] = updatedAdmission;        
    }

    addAdmission(updatedAdmission: Admission)
    {
        console.log(updatedAdmission);       
        this.admissions.push(updatedAdmission);        
    }
}