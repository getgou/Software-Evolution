import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { AppComponent } from './components/app/app.component';
import { MainComponent } from './components/main/main.component';
import {routes} from "./routes";
import {LoginComponent} from "./components/login/login.component";
import {
  MatAutocompleteModule, 
  MatButtonModule, MatCardModule, 
  MatCheckboxModule, MatChipsModule, MatDialogModule,
  MatIconModule, MatFormFieldControl, 
  MatSelectModule, MatOptionModule, MatInputModule,
  MatProgressBarModule, MatProgressSpinnerModule,
  MatStepperModule,
  MatTableModule, MatTabsModule,
  MatToolbarModule, MatTooltipModule, MatFormFieldModule, MAT_DIALOG_DATA
} from "@angular/material";
import { ToolbarComponent } from './components/toolbar/toolbar.component';
import {FlexLayoutModule} from "@angular/flex-layout";
import { NewBuildComponent } from './components/new-build/new-build.component';
import { NewMaterialComponent } from './components/new-material/new-material.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DragulaModule} from "ng2-dragula";
import { AddUserDialogComponent } from './components/new-build/new-build.component';
import {QRCodeModule} from "angular2-qrcode";
import { UserTableComponent } from './components/user-table/user-table.component';
import {HttpClientModule} from "@angular/common/http";
import { BuildsComponent } from './components/builds/builds.component';
import { BuildDetailsComponent } from './components/build-details/build-details.component';
import {QrDialogComponent} from "./components/dialogs/qr-dialog/qr-dialog.component";
import {MarkDoneDialogComponent} from "./components/dialogs/delete-dialog/mark-done-dialog.component";
import {DeleteDialogComponent} from "./components/dialogs/mark-done-dialog/delete-dialog.component";
import {AuthService} from "./services/auth/auth.service";
import {AuthGuard} from "./guards/auth.guard";
import {FileUploadModule} from "ng2-file-upload";
import { CostDialogComponent } from './components/dialogs/cost-dialog/cost-dialog.component';
import { ParameterDialogComponent } from './components/dialogs/parameter-dialog/parameter-dialog.component';
import { BuildService } from './services/build/build.service';

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    LoginComponent,
    ToolbarComponent,
    NewBuildComponent,
    NewMaterialComponent,
    QrDialogComponent,
    MarkDoneDialogComponent,
    DeleteDialogComponent,
    AddUserDialogComponent,
    UserTableComponent,
    BuildsComponent,
    BuildDetailsComponent,
    CostDialogComponent,
    ParameterDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    FileUploadModule,
    FormsModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatCardModule,
    MatTabsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    MatInputModule,
    MatAutocompleteModule,
    MatStepperModule,
    MatTableModule,
    MatChipsModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatTooltipModule,
    MatCheckboxModule,
    FlexLayoutModule,
    DragulaModule,
    QRCodeModule,
    RouterModule.forRoot(routes)
  ],
  providers: [AuthService, AuthGuard, BuildService, { provide: MAT_DIALOG_DATA, useValue: {} }],
  bootstrap: [AppComponent],
  entryComponents: [MarkDoneDialogComponent, DeleteDialogComponent, QrDialogComponent, AddUserDialogComponent, 
    CostDialogComponent, ParameterDialogComponent]
})
export class AppModule { }
