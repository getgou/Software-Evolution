
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
// http.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent
} from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

import {AuthService} from "./auth.service";
@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(public authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authService.isAuthenticated()) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', 'token!')
      });
      return next.handle(authReq);
    } else {
      return next.handle(req);
    }
  }

}
