import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatTableDataSource} from '@angular/material';
import {User} from "../../model/user";

@Component({
  selector: 'app-user-table',
  templateUrl: './user-table.component.html',
  styleUrls: ['./user-table.component.scss']
})
export class UserTableComponent implements OnInit {

  @Input()
  userInput: UserListEntry[] = [];

  public userList: UserListEntry[] = [];

  displayedColumns = ['name'];
  dataSource;

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  constructor() { }

  ngOnInit() {
    this.userInput.forEach(user => {
      this.userList.push({
        checked: user.checked,
        user_id: user.user_id,
        first_name: user.first_name,
        last_name: user.last_name,
        username: user.username
      });
    });
    this.dataSource = new MatTableDataSource(this.userList);
  }

  rowClick(row) {
    console.log(this.userList);
  }
}

export class UserListEntry {
  checked: boolean;
  user_id: number;
  first_name: string;
  last_name: string;
  username: string;

  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }
}
