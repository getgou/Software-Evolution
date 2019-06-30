import { Component, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog, MatIconRegistry } from "@angular/material";
import {FormGroup, FormControl} from "@angular/forms";
import { Router } from "@angular/router";
import {BuildService} from "../../../services/build/build.service";
@Component({
  selector: 'app-parameter-dialog',
  templateUrl: './parameter-dialog.component.html',
  styleUrls: ['./parameter-dialog.component.scss']
})
export class ParameterDialogComponent implements OnInit {
  constParamsForm: FormGroup;
  public printerCost: number;
  public materialCost: number;
  public labourCost: number;
  public preProcessing: number;
  public setupTime: number;
  public powderHandling: number;
  public startTime: number;
  public buildRemovalTime: number;
  public partRemovalTime: number;
  public machineCleaningTime: number;
  public postProcessingTime: number;
  public id = 1;
  constructor(public dialogRef: MatDialogRef<ParameterDialogComponent>, 
    public dialog: MatDialog,
    private buildService: BuildService,
    private router: Router) {
  }

  onClickClose(): void {
    this.dialogRef.close();
  }

  setParameters() {
    this.printerCost = this.constParamsForm.controls['printerCost'].value;
    this.materialCost = this.constParamsForm.controls['materialCost'].value;
    this.labourCost = this.constParamsForm.controls['labourCost'].value;
    this.preProcessing = this.constParamsForm.controls['preProcessing'].value;
    this.setupTime = this.constParamsForm.controls['setupTime'].value;
    this.powderHandling = this.constParamsForm.controls['powderHandling'].value;
    this.startTime = this.constParamsForm.controls['startTime'].value;
    this.buildRemovalTime = this.constParamsForm.controls['buildRemovalTime'].value;
    this.partRemovalTime = this.constParamsForm.controls['partRemovalTime'].value;
    this.postProcessingTime = this.constParamsForm.controls['postProcessingTime'].value;
    this.machineCleaningTime = this.constParamsForm.controls['machineCleaningTime'].value;
    this.buildService.saveConstantParameters(
      this.printerCost, 
      this.materialCost,
      this.labourCost,
      this.preProcessing,
      this.setupTime,
      this.powderHandling,
      this.startTime,
      this.buildRemovalTime,
      this.partRemovalTime,
      this.postProcessingTime,
      this.machineCleaningTime
      ).subscribe(data => {
        console.log("data=" + data);
        alert("Parameters registered successfully!");
    }, error => {
      alert("Something went wrong");
      console.log("Error" + error);
    });
  }
  updateParameters() {
    this.printerCost = this.constParamsForm.controls['printerCost'].value;
    this.materialCost = this.constParamsForm.controls['materialCost'].value;
    this.labourCost = this.constParamsForm.controls['labourCost'].value;
    this.preProcessing = this.constParamsForm.controls['preProcessing'].value;
    this.setupTime = this.constParamsForm.controls['setupTime'].value;
    this.powderHandling = this.constParamsForm.controls['powderHandling'].value;
    this.startTime = this.constParamsForm.controls['startTime'].value;
    this.buildRemovalTime = this.constParamsForm.controls['buildRemovalTime'].value;
    this.partRemovalTime = this.constParamsForm.controls['partRemovalTime'].value;
    this.postProcessingTime = this.constParamsForm.controls['postProcessingTime'].value;
    this.machineCleaningTime = this.constParamsForm.controls['machineCleaningTime'].value;
    this.buildService.updateParameters(
      this.id,
      this.printerCost, 
      this.materialCost,
      this.labourCost,
      this.preProcessing,
      this.setupTime,
      this.powderHandling,
      this.startTime,
      this.buildRemovalTime,
      this.partRemovalTime,
      this.postProcessingTime,
      this.machineCleaningTime
      ).subscribe(data => {
        console.log("data=" + data);
        alert("Parameters updated successfully!");
    }, error => {
      alert("Something went wrong");
      console.log("Error" + error);
    });
  }
  ngOnInit() {
    this.constParamsForm = new FormGroup({
      printerCost: new FormControl(),
      materialCost: new FormControl(),
      labourCost: new FormControl(),
      preProcessing: new FormControl(),
      setupTime: new FormControl(),
      powderHandling: new FormControl(),
      startTime: new FormControl(),
      buildRemovalTime: new FormControl(),
      partRemovalTime: new FormControl(),
      postProcessingTime: new FormControl(),
      machineCleaningTime: new FormControl()
    });
    this.buildService.getParameters(1).subscribe(data => {
    this.printerCost = data.printerCost;
    this.materialCost = data.materialCost;
    this.labourCost = data.labourCost; 
    this.preProcessing = data.preProcessingTime;
    this.setupTime = data.setupTime;
    this.powderHandling = data.powderHandlingTime;
    this.startTime = data.jobStartTime;
    this.buildRemovalTime = data.buildRemovalTime;
    this.partRemovalTime = data.partRemovalTime;
    this.postProcessingTime = data.postProcessingTime;
    this.machineCleaningTime = data.machineCleaningTime;
    });
  }
}
