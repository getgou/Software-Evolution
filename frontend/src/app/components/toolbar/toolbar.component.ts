import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";
import {AuthService} from "../../services/auth/auth.service";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss'],
  providers: [AuthService]
})
export class ToolbarComponent implements OnInit {

  user: User;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.user = this.authService.getUser();
  }

  logout() {
    this.authService.logout();
  }
}
