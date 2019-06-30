import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {BuildService} from "../../services/build/build.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Build} from "../../model/build";
import {DomSanitizer} from "@angular/platform-browser";
import {MatDialog, MatIconRegistry} from "@angular/material";
import {QrDialogComponent} from "../dialogs/qr-dialog/qr-dialog.component";
import {MarkDoneDialogComponent} from "../dialogs/delete-dialog/mark-done-dialog.component";
import {DeleteDialogComponent} from "../dialogs/mark-done-dialog/delete-dialog.component";
import {CostDialogComponent} from "../dialogs/cost-dialog/cost-dialog.component";
import { ParameterDialogComponent } from '../dialogs/parameter-dialog/parameter-dialog.component';
import {PrintingData} from "../../model/printing-data";
import {PrintingDataService} from "../../services/printing-data/printing-data.service";
import {DatePipe} from "@angular/common";
import {FileService} from "../../services/file/file.service";
import * as AWS from "aws-sdk";
import {S3Constants} from "../../model/S3";
import {Utils} from "../../model/utils";
import * as FileSaver from 'file-saver';
import * as jsPDF from 'jspdf';
import 'jspdf-autotable';


@Component({
  selector: 'app-build-details',
  templateUrl: './build-details.component.html',
  styleUrls: ['./build-details.component.scss'],
  providers: [BuildService, PrintingDataService, DatePipe, FileService]
})
export class BuildDetailsComponent implements OnInit {

  build: Build;
  printingData: PrintingData;
  imgString: string;
  estimationData: Object;
  time: number;
  cost: number;
  weight: number;
  constructor(private buildService: BuildService,
              private activatedRoute: ActivatedRoute,
              private iconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer,
              public dialog: MatDialog,
              private router: Router,
              private printingDataService: PrintingDataService,
              private datePipe: DatePipe,
              private fileService: FileService) {
    iconRegistry.addSvgIcon('qrcode',
      domSanitizer.bypassSecurityTrustResourceUrl('assets/qrcode.svg'));
  }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe((params: Params) => {
      let buildId = params.get("qrcode");
      this.buildService.getBuild(buildId).subscribe(build => {
        this.build = build;
        this.getImageFromS3("magicscreenshot/" + this.build.Magic[0].MagicScreenshot);
        this.printingDataService.getPrintingData(this.build.QR_code).subscribe(printingData => {
          this.printingData = printingData;
        }, error => {
          this.printingData = new PrintingData();
          console.log(error);
        });
      }, error => {
        console.log(error);
      });
    });
  }
  toBuildCost() {
    this.router.navigateByUrl('/build/cost');
  }
  private getImageFromS3(key: string) {
    this.getFromS3(key).then(data => {
      console.log("data:", data);
      let base64Image = "data:" + data.ContentType + ";base64," + this.encode(data.Body);
      this.imgString = base64Image;
    }).catch(err => {
      console.log(err);
    });
  }

  private getFromS3(key: string): Promise<any> {
    let s3 = new AWS.S3({apiVersion: S3Constants.apiVersion, region: S3Constants.region});
    s3.config.credentials = {
      accessKeyId: S3Constants.accessKeyId,
      secretAccessKey: S3Constants.secretKey
    };
    s3.config.setPromisesDependency(Promise);

    let params = {
      Bucket: S3Constants.bucket,
      Key: key
    };

    return s3.getObject(params).promise();
  }

  /**
   * Thanks to someone:
   * https://stackoverflow.com/a/33050601/3565973
   * https://stackoverflow.com/a/5366839/3565973
   * @param data
   * @returns {string}
   */
  encode(data) {
    let str = data.reduce(function (a, b) {
      return a + String.fromCharCode(b);
    }, '');
    return btoa(str).replace(/.{76}(?=.)/g, '$&\n');
  }


  markAsDone() {
    let dialogRef = this.dialog.open(MarkDoneDialogComponent, {
      width: '500px',
      data: {obj: this.build}
    });
  }

  replicate() {
    // TODO: implement
  }

  jsontopdf() {
    const doc = new jsPDF();
    const col = ["ID", "Parameter", "Value"];
    const rows = [];

 // The following array of object as response from the API req 

   const itemNew = [    
    { id: 1, name: 'Z-height', value : 10.5},
    { id: 2, name: 'No of layers', value : 350 },
    { id: 3, name: 'Powder Used', value: 3308 },
    { id: 4, name: "Weight", value: 3.7 }, 
    { id: 5, name: "Total powder used", value: 3308}
  ];


 itemNew.forEach(element => {      
      const temp = [element.id, element.name, element.value];
      rows.push(temp);

  });        

     doc.text ("List of parameters used:", 15, 10);
     doc.autoTable(col, rows);
     window.open(doc.output('save', 'List of Parameters.pdf'));
     // doc.output('save', 'List of Parameters.pdf');
     // doc.save(window.open());
     // doc.save('List of Parameters.pdf');

     // const x = window.open();
      
    }


  downloadFiles() {
    let fileName;
    (this.build.SLM as any).forEach(slmObj => {
      if (slmObj["FileName"] != null) {
        fileName = slmObj["FileName"];
        this.downloadFile(fileName);
      }
    });

    this.build.Magic.forEach(magicsObj => {
      if (magicsObj.FileName != null) {
        fileName = magicsObj.FileName;
        this.downloadFile(fileName);
      }
      if (magicsObj.MagicScreenshot != null) {
        fileName = magicsObj.MagicScreenshot;
        this.downloadFile(fileName);
      }
    });

    this.build.Part.forEach(partObj => {
      if (partObj.StlFileName != null) {
        fileName = partObj.StlFileName;
        this.downloadFile(fileName);
      }
      if (partObj.PrtFileName != null) {
        fileName = partObj.PrtFileName;
        this.downloadFile(fileName);
      }
    });
  }

  private downloadFile(fileName: string) {
    let key = Utils.getPathFromSuffix(Utils.getSuffix(fileName)) + "/" + fileName;
    console.log(key);
    this.getFromS3(key).then(data => {
      console.log("DATA!!!", data);
      let blob = new Blob([data.Body], {type: data.ContentType});

      FileSaver.saveAs(blob, fileName);

    }).catch(err => {
      console.log(err);
    });
  }

  showBuildId() {
    this.showQRCode([this.build.QR_code], "Build ID", null);
  }

  showPartIds() {
    let partQRCodes = [];
    let magicIDs: string[] = [];
    this.build.Part.forEach(part => {
      partQRCodes.push(part.QR_code);
      magicIDs.push(part.MagicID);
    });
    this.showQRCode(partQRCodes, "Part ID", magicIDs);
  }

  showMaterialId() {
    this.showQRCode(["M" + this.build.MaterialId], "Material ID", null);
  }


  deleteBuild() {
    let dialogRef = this.dialog.open(DeleteDialogComponent, {
      width: '500px',
      data: {obj: this.build}
    });
  }
  showInputParameters() {
    let dialogRef = this.dialog.open(ParameterDialogComponent, {
      width: '600px',
      data: "{obj: this.build}"
    });
  }
  showCost(id: string) {
    let idd = Number(id.substr(1));
    this.buildService.getParameter(idd).subscribe(data => {
      console.log(data);
      this.time = data.totalPrintingTime;
      this.cost = data.totalCost;
      this.weight = data.totalPowderUsed;
      let dialogRef = this.dialog.open(CostDialogComponent, {
        width: '500px',
        data: {etime: this.time, ecost: this.cost, eweight: this.weight}
      });
  }, error => {
    alert("Something went wrong");
    console.log("Error" + error);
  });
    /*if (this.time && this.cost && this.weight) {
      let dialogRef = this.dialog.open(CostDialogComponent, {
        width: '500px',
        data: {etime: this.time, ecost: this.cost, eweight: this.weight}
      });
    }*/
  }
 
  private showQRCode(qrcode: string[], type: string, magicIDs: string[]) {
    let dialogRef = this.dialog.open(QrDialogComponent, {
      width: '500px',
      data: {qrcodes: qrcode, type: type, magicIDs: magicIDs}
    });
  }
}
