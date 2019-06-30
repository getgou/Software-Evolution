import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {Component, Inject} from "@angular/core";

@Component({
  selector: 'app-qr-dialog',
  styleUrls: ['../dialog.component.scss'],
  templateUrl: './qr-dialog.component.html',
})
export class QrDialogComponent {

  constructor(public dialogRef: MatDialogRef<QrDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  onClickClose(): void {
    this.dialogRef.close();
  }
}
