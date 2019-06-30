import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog, MatIconRegistry } from "@angular/material";
import { BuildService } from "../../../services/build/build.service";
import {ParameterDialogComponent} from '../parameter-dialog/parameter-dialog.component';
import { Router } from "@angular/router";
@Component({
  selector: 'app-cost-dialog',
  templateUrl: './cost-dialog.component.html',
  styleUrls: ['./cost-dialog.component.scss']
})
export class CostDialogComponent implements OnInit {
  public preProcessingCost;
  public postProcessingCost;
  public totalCost;
  public fileName: string;
  public stlName: string;
  public numLayers: number;
  public zHeight: number;
  public estimatedPowder: number;
  public estimatedTime: number;
  public estimatedCost: number;
  constructor(public dialogRef: MatDialogRef<CostDialogComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialog: MatDialog,
    private router: Router) {  
  }

  onClickClose(): void {
    this.dialogRef.close();
  }

  showInputParameters() {
    let dialogRef = this.dialog.open(ParameterDialogComponent, {
      width: '600px',
      data: "{obj: this.build}"
    });
  }
  ngOnInit() {
      this.estimatedPowder = this.data.eweight;
      this.estimatedCost = this.data.ecost;
      this.estimatedTime = this.data.etime;
  }
}
