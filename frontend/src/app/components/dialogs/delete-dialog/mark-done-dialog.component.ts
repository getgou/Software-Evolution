import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Component, Inject} from "@angular/core";
import {BuildService} from "../../../services/build/build.service";
import {Router} from "@angular/router";
import {Build} from "../../../model/build";

@Component({
  selector: 'app-mark-done-dialog',
  styleUrls: ['../dialog.component.scss'],
  templateUrl: './mark-done-dialog.component.html',
  providers: [BuildService]
})
export class MarkDoneDialogComponent {

  constructor(public dialogRef: MatDialogRef<MarkDoneDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private buildService: BuildService,
              private router: Router) {
  }

  onClickClose(): void {
    this.dialogRef.close();
  }

  onClickMarkDone() {
    let updatedBuild = new Build(this.data.obj);
    updatedBuild.Status = 2;
    this.buildService.updateBuild(updatedBuild).subscribe(data => {
      this.data.obj.Status = 2;
      this.dialogRef.close();
    }, error => {
      console.log(error);
      this.dialogRef.close();
    });
  }
}
