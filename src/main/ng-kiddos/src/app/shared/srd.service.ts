export class SrdData{
    key: number;
    value: string;
}

export class SrdService{
    getPrograms()
    {
        let programs : SrdData[] = [
            {key:1, value:'DC : Ad Hoc : A day at HMI'}, 
            {key:2, value:'DC : Ad Hoc : A week at HMI'}, 
            {key:3, value:'IC : Ad Hoc : A day at HMI'}, 
            {key:4, value:'IC : Ad Hoc : A week at HMI'}, 
        ];
        return programs;    
    }
}