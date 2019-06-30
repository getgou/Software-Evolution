import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth/auth.service";
import {User} from "../../model/user";
import {Router} from "@angular/router";
import {BuildService} from "../../services/build/build.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  providers: [AuthService, BuildService]
})
export class MainComponent implements OnInit {
  private user: User;
  searchBuildID = "B";

  constructor(private authService: AuthService,
              private router: Router,
              private buildService: BuildService) { }

  ngOnInit() {
    this.user = this.authService.getUser();
  }

  search() {
    let id = this.searchBuildID;
    this.router.navigate(["build", id]);
  }

}
