import { Injectable } from '@angular/core';
import {of} from "rxjs/observable/of";
import {User} from "../../model/user";
import {Observable} from "rxjs/Observable";

@Injectable()
export class UserService {

  private users: User[] = [];

  constructor() {
    let user = new User({"user_id": 0, "first_name": "Tester", "last_name": "Testsson", "username": "tester123"});
    this.users.push(user);
    user = new User({"user_id": 1, "first_name": "Kalle", "last_name": "Kula", "username": "kalle32"});
    this.users.push(user);
    user = new User({"user_id": 2, "first_name": "Johan", "last_name": "Jordnötsson", "username": "johan123"});
    this.users.push(user);
  }


  getAllUsers(): Observable<User[]> {
    return of(this.users);
  }

}
