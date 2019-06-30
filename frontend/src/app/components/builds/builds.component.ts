import {Component, Input, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material';
import {BuildService} from "../../services/build/build.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-builds',
  templateUrl: './builds.component.html',
  styleUrls: ['./builds.component.scss'],
  providers: [BuildService]
})
export class BuildsComponent implements OnInit {
  @Input()
  filterOnMarkedAsDone = false;

  public builds: BuildEntry[] = [];

  private displayedColumns = ['qrcode'];
  private dataSource;
  loaded: boolean | null = false;

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  constructor(private buildService: BuildService,
              private router: Router) { }

  ngOnInit() {
    this.buildService.getAllBuilds().subscribe(allBuilds => {
      let inProgressBuilds;
      if (this.filterOnMarkedAsDone) {
        inProgressBuilds = allBuilds.filter(build => {
          return build.Status !== 2;
        });
      } else {
        inProgressBuilds = allBuilds;
      }

      inProgressBuilds.forEach(build => {
        this.builds.push({
          qrcode: build.QR_code
        });
      });

      // this.builds = [new BuildEntry({qrcode: "hej"})];

      this.loaded = true;

      this.dataSource = new MatTableDataSource(this.builds);
    }, error => {
      console.log(error);
      this.loaded = null;
    });
    //
    this.details();
    //
  }

  rowClick(row) {
    console.log(row);
    this.router.navigate(["build", row.qrcode]);
  }

  details() {
    //const stl = NodeStl("D1.stl");
    //console.log(stl.volume);
    //console.log(stl.weight);
  }
}

export class BuildEntry {
  qrcode: string;

  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }
}
