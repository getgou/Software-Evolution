import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class FileService {

  private headers: HttpHeaders;

  constructor(private httpClient: HttpClient) {
    // this.headers.set()
  }

  uploadFile(url: string): Observable<null> {
    return this.httpClient.get<null>(url, {headers: this.headers});
  }

  downloadFile(url: string): Observable<null> {
    return this.httpClient.get<null>(url, {headers: this.headers});
  }
}
