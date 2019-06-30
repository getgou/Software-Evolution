import {Magic} from "./magics";
import {Part} from "./part";
import {User} from "./user";

export class Build {
  BuildID: number;
  QR_code: string;
  MaterialId: number;
  // materialQrcode: string;
  markedAsDone: boolean;
  Status: number;
  Magic: Magic[] = [];
  SLM: string;
  GomBuild: string[] = [];
  Part: Part[] = [];
  fullAccessUsers: User[] = [];
  readAccessUsers: User[] = [];


  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }

  clearAllExceptAccess() {
    this.BuildID = null;
    this.MaterialId = null;
    this.Magic = [];
    this.SLM = null;
    this.GomBuild = [];
    this.Part = [];
  }
}
