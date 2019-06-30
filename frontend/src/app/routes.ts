import {Routes} from "@angular/router";
import {AppComponent} from "./components/app/app.component";
import {MainComponent} from "./components/main/main.component";
import {LoginComponent} from "./components/login/login.component";
import {NewBuildComponent} from "./components/new-build/new-build.component";
import {NewMaterialComponent} from "./components/new-material/new-material.component";
import {BuildDetailsComponent} from "./components/build-details/build-details.component";
import {BuildsComponent} from "./components/builds/builds.component";
import {UserTableComponent} from "./components/user-table/user-table.component";
import {AuthGuard} from "./guards/auth.guard";


export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    component: MainComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'build/new',
    component: NewBuildComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'build/:qrcode',
    component: BuildDetailsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'material/new',
    component: NewMaterialComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '**',
    canActivate: [AuthGuard],
    redirectTo: ''
  }
];
