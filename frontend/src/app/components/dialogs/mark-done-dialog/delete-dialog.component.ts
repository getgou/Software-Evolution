import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Component, Inject} from "@angular/core";
import {BuildService} from "../../../services/build/build.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-delete-dialog',
  styleUrls: ['../dialog.component.scss'],
  templateUrl: './delete-dialog.component.html',
  providers: [BuildService]
})
export class DeleteDialogComponent {

  constructor(public dialogRef: MatDialogRef<DeleteDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private buildService: BuildService,
              private router: Router) {
  }

  onClickClose(): void {
    this.dialogRef.close();
  }

  onClickDelete() {
    this.buildService.deleteBuild(this.data.obj).subscribe(data => {
      this.dialogRef.close();
      this.router.navigateByUrl('');
    }, error => {
      alert(error);
      this.dialogRef.close();
    });
  }
}
