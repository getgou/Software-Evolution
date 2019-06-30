import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth/auth.service";
import {Credentials} from "../../model/credentials";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [AuthService]
})
export class LoginComponent implements OnInit {
  private credentials: Credentials = new Credentials();
  loginForm: FormGroup;


  constructor(private router: Router,
              private authService: AuthService,
              private fb: FormBuilder) {
    this.loginForm = fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit() {
  }

  login() {
    this.credentials.Username = this.loginForm.get("username").value;
    this.credentials.Password = this.loginForm.get("password").value;

    // this.credentials.Username = "SuperPowerUser";
    // this.credentials.Password = "password";




     console.log(JSON.stringify(this.credentials));
     console.log("hej", this.authService.verifyUser(this.credentials)); // .subscribe(data => {


    // this.authService.data['access_token'];

    // });
  }
}
