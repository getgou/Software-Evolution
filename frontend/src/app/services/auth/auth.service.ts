import {Injectable} from '@angular/core';
import {User} from "../../model/user";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {of} from "rxjs/observable/of";
import {API} from "../../model/API";
import {Credentials} from "../../model/credentials";

@Injectable()
export class AuthService {
  private user: User;

  constructor(private router: Router,
              private httpClient: HttpClient) {
    let user = new User({
      "user_id": 0, "first_name": "Tester", "last_name":
        "Testsson", "username": "tester123"
    });
    this.user = user;
  }

  public getUser() {
    return this.user;
  }

  public getToken(): string {
    return localStorage.getItem('token');
  }

  verifyUser(credentials: Credentials) {

    /*
    alert("Skickar förfrågan till: " + API.Account.getMyCall());
    // "http://gotserv17/webapi/api/Account/MyCall"
    this.httpClient.get(API.Account.getMyCall()).subscribe(data => {
      alert("Från My Call: " + data);
    }, error => {
      // TODO: handle error
      // console.log("Hittade inte servern :( ", error);
      alert("Hittade inte servern :(  " + error.toString());
    });

    this.router.navigateByUrl('');
    */

    let query = "grant_type=password&Username=" + credentials.Username + "&Password=" + credentials.Password;
    return this.httpClient.post<Object>(API.Base.TOKEN, query, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
      }
    })
      .subscribe(data => {
        console.log(data);
        localStorage.setItem('token', data['access_token']);
        // localStorage.setItem('token_type', data['token_type']);
        // localStorage.setItem('expires_in', data['expires_in']);
        this.router.navigateByUrl('');
      }, error => {
        // TODO: handle error
        console.log("Invalid credentials: ", error);
        alert("Invalid credentials!");
      });



  }

  logout() {
    // this.user = null;
    localStorage.removeItem('token'); // drop token
    this.router.navigateByUrl("login");
  }

  isAuthenticated(): boolean {
    return this.getToken() != null;
  }
}
