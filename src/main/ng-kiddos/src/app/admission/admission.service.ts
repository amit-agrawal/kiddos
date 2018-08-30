export class Admission{
    firstName:string;
    lastName:string;
    age:string;
    term_month: string;
    program: string;
    batch_plan: string;
    admissionDate: string

    constructor(firstName:string,
        lastName:string,
        age:string,
        term_month: string,
        program: string,
        batch_plan: string,
        admissionDate: string)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.term_month = term_month;
        this.program = program;
        this.batch_plan = batch_plan;
        this.admissionDate = admissionDate;
    }
}

export class AdmissionService{
    admissions: Admission[] = [
        new Admission('Sid',
            'Sharma',
            '3 years 2 months',
            'March 18',
            'PG',
            'Morning',
            '2-2-2018'),
         new Admission( 'Adwika',
            'Chauvan',
            '4 years 1 months',
            'March 17',
            'DC',
            'Evening',
            '2-2-2017'),
          new Admission( 'Myra',
            'Deshpande',
            '4 years 6 months',
            'March 16',
            'DC',
            'Morning',
            '2-2-2016')
    ];
    getAllAdmissions()
    {
        //todo
       return this.admissions;
    }
}