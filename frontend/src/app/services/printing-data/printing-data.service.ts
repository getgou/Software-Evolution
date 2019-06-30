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
import {PrintingData} from "../../model/printing-data";
import {AuthService} from "../auth/auth.service";

@Injectable()
export class PrintingDataService {
  private headers: HttpHeaders;
  private mockData: PrintingData;

  constructor(private httpClient: HttpClient,
              private authService: AuthService) {
    this.headers = new HttpHeaders().set(
      'Authorization', 'bearer ' + authService.getToken()
    );

    this.mockData = new PrintingData({printingInfoID: 1,
      buildStatus: "test",
      startTime: "2017-11-07T12:00:00",
      endTime: "2017-11-07T14:20:00",
      printingDate: "2017-11-15T00:00:00",
      operator: "Test ",
      typeOfmachine: "Test",
      powderWeightStart: 2.3,
      powderweightEnd: 3.5,
      weightPowderWaste: 5.2,
      powerUsed: 1.05,
      platformMaterial: "Test",
      platformWeight: 5.02,
      printedTime: "2017-11-07T11:08:00",
      powderCondition: "Test",
      numberLayers: 3,
      dpcFactor: 0,
      minExposureTime: "2017-11-15T00:00:00",
      comments: "Some comments about this build...",
      buildId: 28});


  }

  public getPrintingData(qrcode: string): Observable<PrintingData> {
    return this.httpClient
      .get<PrintingData>(API.PrintingData.getQrcode(qrcode), {headers: this.headers});
    // return of(this.mockData);
  }
}
