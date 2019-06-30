import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {AuthService} from "../services/auth/auth.service";
import {of} from "rxjs/observable/of";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService,
              private router: Router) {

  }

  canActivate(): boolean {
    if (this.authService.isAuthenticated()) {
      return true;
    } else {
      this.router.navigateByUrl("/login");
      return false;
    }
  }
}
