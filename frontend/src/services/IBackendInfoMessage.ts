export interface IBackendInfoMessage {
    topicname: string;
    operation: string;
    id: number;
}

export class BackendInfo implements IBackendInfoMessage {
    topicname = ""; 
    operation = "";
    id = 0;

    constructor(tname: string, op: string, idNum: number){
        this.topicname = tname;
        this.operation = op;
        this.id = idNum;
    }
}