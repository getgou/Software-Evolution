import {Injectable} from '@angular/core';
import {Build} from "../../model/build";
import {Response} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {API} from "../../model/API";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {of} from "rxjs/observable/of";
import {Part} from "../../model/part";
import {User} from "../../model/user";
import {Magic} from "../../model/magics";
import {AuthService} from "../auth/auth.service";
import {PrintingData} from "../../model/printing-data";
import { AnalyticsConfiguration } from 'aws-sdk/clients/s3';

@Injectable()
export class BuildService {
  private headers: HttpHeaders;
  private mockBuild;

  constructor(private httpClient: HttpClient,
              private authService: AuthService) {
    this.headers = new HttpHeaders().set(
      'Authorization', "bearer " + this.authService.getToken()
    );


    this.mockBuild = new Build();
    this.mockBuild.BuildID = 1234;
    this.mockBuild.QR_code = "B1234";
    this.mockBuild.MaterialId = 12;
    this.mockBuild.materialQrcode = "M12";
    this.mockBuild.markedAsDone = false;
    let magics = new Magic();
    magics.MagicScreenshot = "assets/magics_img.jpg";
    magics.FileName = "file.Magic";
    this.mockBuild.Magic = magics;
    this.mockBuild.SLM = "file.SLM";
    this.mockBuild.GomBuild = ["file1.gom", "file2.gom"];
    let part = new Part();
    part.magicsIDs = ["1"];
    part.StlFileName = "1.stl";
    part.PrtFileName = "1.prt";
    part.GomPart = ["1.1.gom", "1.2.gom"];
    this.mockBuild.Part = [part];
    let users = [
      new User({"user_id": 0, "first_name": "Tester", "last_name": "Testsson", "username": "tester123"}),
      new User({"user_id": 1, "first_name": "Kalle", "last_name": "Kula", "username": "kalle32"}),
      new User({"user_id": 2, "first_name": "Johan", "last_name": "Jordnötsson", "username": "johan123"})
    ];
    this.mockBuild.fullAccessUsers = [users[0]];
    this.mockBuild.readAccessUsers = users.splice(0, 1);
  }

  public getAllBuilds(): Observable<Build[]> {
    return this.httpClient
      .get<Build[]>(API.Build.getAllBuilds, {headers: this.headers});
    // let mockBuild2 = new Build(this.mockBuild);
    // let mockBuild3 = new Build(this.mockBuild);
    // mockBuild2.QR_code = "B42";
    // mockBuild2.markedAsDone = true;
    // mockBuild3.QR_code = "B12";
    // mockBuild3.markedAsDone = true;
    // return of([this.mockBuild, mockBuild2, mockBuild3]);
  }

  public getBuild(qrcode: string): Observable<Build> {
    return this.httpClient.get<Build>(API.Build.getBuild(qrcode),
      {headers: this.headers});
    // return Observable.throw("");
    // return of(this.mockBuild);
  }

  public createBuild(build: Build, params): Observable<string> {
    let partJson = [];
    build.Part.forEach(part => {
      partJson.push({
        prt: part.PrtFileName,
        stl: part.StlFileName,
        gompart: [],
        magicsIDs: part.magicsIDs
      });
    });

    let json = {
      build: {
        materialId: 2,
        fileName: params.fileName,
        unit: params.unit,
        materialType: params.materialType,
        infillPercentage: params.infillPercentage,
        volume: params.volume,
        weight: params.weight,
        zheight: params.zheight,
        numLayers: params.numLayers,

        magic: {
          filename: build.Magic[0].FileName,
          magicscreenshot: build.Magic[0].MagicScreenshot
        },
        slm: build.SLM,
        gombuilds: build.GomBuild,
        parts: partJson
      }
    };
    console.log(json);
    return this.httpClient.post<string>(API.Build.createBuild, json,
      {
        headers: this.headers
      });
  }

  public deleteBuild(build: Build): Observable<boolean> {
    // return this.httpClient.delete<string>(API.Build.deleteBuild,
    //   {
    //     headers: this.headers
    //   });
    return of(true); // TODO: implement
  }

  private handleError(error: any) {
    // TODO: handle 404 etc.
    console.error("Error: ", error);
  }

  updateBuild(build: Build): Observable<boolean> {
    console.log(API.Build.updateBuild(build.QR_code));
    let json = {
      build:
        {
          status: build.Status
        }
    };
    return this.httpClient.put<boolean>(API.Build.updateBuild(build.QR_code), json,
      {
        headers: this.headers
      });
    // return of(true); // TODO: implement
  }

  createPrintingInfo(data: string): Observable<number> {
    let json = {};
    json["buildId"] = data;
    console.log("Printing info: ", json);
    return this.httpClient.post<number>(API.PrintingData.Create,
      data,
      {headers: this.headers});
  }
  public saveConstantParameters(
    printercost,
    materialcost,
    labourcost,
    preprocessingtime,
    setuptime,
    powderhandlingtime,
    jobstarttime,
    buildremovaltime,
    partremovaltime,
    postprocessingtime,
    machinecleaningtime
  ): Observable<number> {
    let ConstantParameters = {
        PrinterCost: printercost,
        MaterialCost: materialcost,
        LabourCost: labourcost,
        PreProcessingTime: preprocessingtime,
        SetupTime: setuptime,
        PowderHandlingTime: powderhandlingtime,
        JobStartTime: jobstarttime,
        BuildRemovalTime: buildremovaltime,
        PartRemovalTime: partremovaltime,
        PostProcessingTime: postprocessingtime,
        MachineCleaningTime: machinecleaningtime
    };
    return this.httpClient.post<number>(API.Parameters.Register, ConstantParameters, {
      headers: this.headers
    });
  }
  public updateParameters(
    id,
    printercost,
    materialcost,
    labourcost,
    preprocessingtime,
    setuptime,
    powderhandlingtime,
    jobstarttime,
    buildremovaltime,
    partremovaltime,
    postprocessingtime,
    machinecleaningtime
  ): Observable<any> {
    let ConstantParameters = {
        ConstID: id,
        PrinterCost: printercost,
        MaterialCost: materialcost,
        LabourCost: labourcost,
        PreProcessingTime: preprocessingtime,
        SetupTime: setuptime,
        PowderHandlingTime: powderhandlingtime,
        JobStartTime: jobstarttime,
        BuildRemovalTime: buildremovaltime,
        PartRemovalTime: partremovaltime,
        PostProcessingTime: postprocessingtime,
        MachineCleaningTime: machinecleaningtime
    };
    return this.httpClient.put<any>(API.Parameters.updateParameters(id), ConstantParameters, {
      headers: this.headers
    });
  }
  public  getParameters(id): Observable<any> {
    return this.httpClient.get<any>(API.Parameters.getParameters(id), {
      headers: this.headers
    });
  }
  public getParameter(id: number): Observable<any> {
    return this.httpClient.get<any>(API.Parameter.getParameter(id), {
      headers: this.headers
    });
  }
}
