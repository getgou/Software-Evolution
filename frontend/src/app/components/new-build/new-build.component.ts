import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, FormControl} from "@angular/forms";
import {FileItem, FileUploader} from "ng2-file-upload";
import {AuthService} from "../../services/auth/auth.service";
import {User} from "../../model/user";
import {DragulaService} from "ng2-dragula";
import {Build} from "../../model/build";
import {Utils} from "../../model/utils";
import {Magic} from "../../model/magics";
import {Part} from "../../model/part";
import {API} from "../../model/API";
import {BuildService} from "../../services/build/build.service";
import {MAT_DIALOG_DATA, MatChipInputEvent, MatDialog, MatDialogRef} from '@angular/material';
import {Router} from "@angular/router";
import {COMMA, ENTER, RIGHT_ARROW, SPACE} from "@angular/cdk/keycodes";
import {UserService} from "../../services/user/user.service";
import {UserListEntry, UserTableComponent} from "../user-table/user-table.component";
import {QrDialogComponent} from "../dialogs/qr-dialog/qr-dialog.component";
import * as AWS from "aws-sdk";
import {S3Constants} from "../../model/S3";


@Component({
  selector: 'app-new-build',
  templateUrl: './new-build.component.html',
  styleUrls: ['./new-build.component.scss'],
  providers: [AuthService, BuildService]
})
export class NewBuildComponent implements OnInit {
  formGroupUpload: FormGroup;
  formGroupOrganize: FormGroup;
  formGroupParameter: FormGroup;
  public uploader: FileUploader;
  public hasDropZoneOver = false;
  private user: User;
  build: Build;
  private fileNamesToMap: string[] = [];
  unmappedFiles: string[] = [];
  private utils = Utils;
  uploadedFiles: FileItem[] = [];
  creatingBuild = false;
  fileName = "";
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;

  // Enter, comma
  separatorKeysCodes = [ENTER, SPACE, RIGHT_ARROW, COMMA];

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private dragulaService: DragulaService,
              private buildService: BuildService,
              public dialog: MatDialog,
              private router: Router) {
    this.user = authService.getUser();
    this.build = new Build();
    this.build.fullAccessUsers.push(this.user);
    console.log(this.build);

    dragulaService.setOptions('bag', {
      revertOnSpill: true
    });

    dragulaService.dropModel.subscribe(value => {
      console.log("drop:", value);
    });

    dragulaService.removeModel.subscribe(value => {
      console.log("remove:", value);
    });

    this.formGroupUpload = formBuilder.group({
      // files: [Validators.required]
    });
    this.formGroupOrganize = formBuilder.group({
      // field1: [Validators.required]
    });
   
    this.uploader = new FileUploader({
      authToken: "bearer " + this.authService.getToken(),
      disableMultipart: true
    });
    // About disableMultipart:
    // "If 'true', disable using a multipart form for file upload and instead
    // stream the file. Some APIs (e.g. Amazon S3) may expect the file to be
    // streamed rather than sent via a form. Defaults to false."

    this.uploader.onBeforeUploadItem = (fileItem: FileItem): any => {
      // logic of connecting url with the file
      fileItem.url = API.Files.getS3Path(fileItem._file.name);

      return {fileItem};
    };

    this.uploader.onAfterAddingFile = (fileItem: FileItem): any => {
      console.log(fileItem._file);
      this.uploadFile();
    };
  }

  ngOnInit() {
    
  }
  createBuild() {
    if (!this.creatingBuild) {
      this.creatingBuild = true;
      console.log(JSON.stringify(this.build));
      let params = {
        fileName: this.fileName
      };
      this.buildService.createBuild(this.build, params).subscribe(data => {
        this.build.QR_code = "B" + data;
        this.showQRCode();
        // this.buildService.createPrintingInfo(data).subscribe(buildId => {
        // }, error => {
        //   alert("Something went wrong creating the printing info");
        //   console.log(error);
        // });
      }, error => {
        alert("Something went wrong creating the build. Try again later.");
        console.log(error);
        this.creatingBuild = false;
      });
    }
  }

  private showQRCode() {
    let dialogRef = this.dialog.open(QrDialogComponent, {
      width: '300px',
      data: {qrcodes: [this.build.QR_code], type: "Build ID"}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.router.navigateByUrl('');
    });
  }

  addUserToPermissionList(list: User[], listname: string) {
    console.log(list);
    let dialogRef = this.dialog.open(AddUserDialogComponent, {
      minWidth: '300px',
      data: {permissionsList: list}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) { // = click done, not dismiss
        let newUserList: User[] = [];
        console.log('The dialog was closed', result);
        let filteredResult = result.filter(resItem => {
          return resItem.checked;
        });
        console.log(filteredResult);
        filteredResult.forEach(item => {
          delete item.checked;
          console.log(item);
          newUserList.push(new User(item));
        });
        list = newUserList;
        if (listname === "fullAccessUsers") {
          this.build.fullAccessUsers = newUserList;
        } else if (listname === "readAccessUsers") {
          this.build.readAccessUsers = newUserList;
        }
      }
    });

  }

  public fileOverDropZone(e: any): void {
    this.hasDropZoneOver = e;
  }

  uploadFile() {
    let s3 = new AWS.S3({apiVersion: S3Constants.apiVersion, region: S3Constants.region});
    s3.config.credentials = {
      accessKeyId: S3Constants.accessKeyId,
      secretAccessKey: S3Constants.secretKey
    };
    s3.config.setPromisesDependency(Promise);

    console.log(this.uploader.getNotUploadedItems());

    this.uploader.getNotUploadedItems().forEach(queueObj => {
      console.log(queueObj._file);
      let fileName = queueObj._file.name;
      let suffix = Utils.getSuffix(fileName);
      console.log("suffix", suffix);
      if (suffix.length > 0 && Utils.isValidFileFormat(suffix) &&
        !this.uploadedFiles.some(fileItem => {
          return fileItem._file.name === queueObj._file.name;
        })) {
        console.log("is valid");

        this.fileNamesToMap.push(fileName);
        let prefix = Utils.getPathFromSuffix(suffix);
        let params = {
          Bucket: S3Constants.bucket,
          Key: prefix + "/" + queueObj._file.name,
          Body: queueObj._file
        };

        let putObjectPromise = s3.putObject(params).promise();
        putObjectPromise.then(data => {
          console.log("DATA#!: ", data);
        }).catch(err => {
          console.log("ERR#!: ", err);
        });

        let uploaded = this.uploader.queue.splice(0, 1);
        this.uploadedFiles = this.uploadedFiles.concat(uploaded);
      } else {
        // wrong file ending or wrong format
        this.filterFaultyItemFromQueue(queueObj._file);
      }
    });

    this.structureFiles();
  }


  private mapToBuildModel(fileName: string) {
    let suffix = Utils.getSuffix(fileName);
    if (suffix === 'magics') {
      if (this.build.Magic.length === 0) {
        this.build.Magic.push(new Magic());
      }
      this.build.Magic[0].FileName = fileName;
    } else if (suffix === 'png' ||
      suffix === 'jpg' ||
      suffix === 'jpeg' ||
      suffix === 'gif') {
      console.log(this.build);
      if (this.build.Magic.length === 0) {
        this.build.Magic[0] = new Magic();
      }
      this.build.Magic[0].MagicScreenshot = fileName;
    } else if (suffix === 'slm') {
      this.build.SLM = fileName;
    } else if (suffix === 'stl') {
      // if (this.build.Part == null) {
      //   this.build.Part = [];
      // }

      // If the file list to map contains a prt-file with the same name, then
      //     if that prt-file is in a build.part, then
      //         add the stl it to that part
      //     else
      //         create a new part and add the stl there
      // else
      //     create a new part and add the stl there
      this.fileName = fileName;
      let possiblePrt = this.fileNamesToMap.find(fileNameInList => {
        return fileNameInList === (Utils.removeSuffix(fileName) + ".prt");
      });

      if (possiblePrt != null) {
        let part = this.build.Part.find(partOfParts => {
          console.log("partOfParts: ", partOfParts);
          return partOfParts.PrtFileName != null &&
            Utils.removeSuffix(partOfParts.PrtFileName) === Utils.removeSuffix(fileName);
        });
        if (part != null) {
          part.StlFileName = fileName;
        } else {
          // if there is no part with that name yet
          part = new Part();
          part.StlFileName = fileName;
          this.build.Part.push(part);
        }
      } else {
        let part = new Part();
        part.StlFileName = fileName;
        this.build.Part.push(part);
      }
    } else if (suffix === 'prt') {
      // if (this.build.Part == null) {
      //   this.build.Part = [];
      // }

      // If the file list to map contains a stl-file with the same name, then
      //     if that stl-file is in a build.part, then
      //         add the prt it to that part
      //     else
      //         create a new part and add the prt there
      // else
      //     create a new part and add the prt there

      let possibleStl = this.fileNamesToMap.find(fileNameInList => {
        return fileNameInList === (Utils.removeSuffix(fileName) + ".stl");
      });
      console.log("possibleSTL: ", possibleStl);
      if (possibleStl != null) {
        let part = this.build.Part.find(partOfParts => {
          console.log("partOfParts: ", partOfParts);
          return partOfParts.StlFileName != null &&
            Utils.removeSuffix(partOfParts.StlFileName) === Utils.removeSuffix(fileName);
        });
        if (part != null) {
          part.PrtFileName = fileName;
        } else {
          // if there is no part with that name yet
          part = new Part();
          part.PrtFileName = fileName;
          this.build.Part.push(part);
        }
      } else {
        this.unmappedFiles.push(fileName);
      }
    }
  }

  filterFaultyItemFromQueue(item: File) {
    console.log("FAIL!");
    let foundItem = this.uploader.queue.find(queueItem => {
      return queueItem._file.name === item.name;
    });
    console.log("Found item: ", foundItem);
    this.uploader.queue.splice(this.uploader.queue.indexOf(foundItem), 1);
  }

  structureFiles() {
    this.build.clearAllExceptAccess();
    this.unmappedFiles = [];

    console.log("click!");

    this.fileNamesToMap.forEach(fileName => {
      this.mapToBuildModel(fileName);
    });

    // TODO: sort the Part

    console.log("************");
    console.log(this.build);
    console.log("unmappedFiles: ", this.unmappedFiles);
    console.log("------------");
  }


  add(part: Part, event: MatChipInputEvent): void {
    let input = event.input;
    let value = event.value;

    // Add our person
    if ((value || '').trim()) {
      part.magicsIDs.push(value.trim());
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  removeMagicsID(part: Part, buildID: string): void {
    this.removeFromListHelper(part.magicsIDs, buildID);
  }

  removeUserPermission(userList: User[], faUser: User) {
    this.removeFromListHelper(userList, faUser);
  }

  private removeFromListHelper(list: any, object: any) {
    let index = list.indexOf(object);

    if (index >= 0) {
      list.splice(index, 1);
    }
  }

  removeUploadedItem(index: number) {
    this.uploadedFiles.splice(index, 1);
  }
}


@Component({
  selector: 'app-add-user-dialog',
  styleUrls: ['../dialogs/dialog.component.scss'],
  templateUrl: '../dialogs/add-user-dialog.component.html',
  providers: [UserService]
})
export class AddUserDialogComponent {
  allUsers: UserListEntry[] = [];
  loaded = false;

  @ViewChild(UserTableComponent) userTableComponent: UserTableComponent;

  constructor(public dialogRef: MatDialogRef<AddUserDialogComponent>,
              private userService: UserService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.userService.getAllUsers().subscribe(usersData => {
      usersData.forEach(user => {
        // this.allUsers.push()
        let isChecked = data.permissionsList.some((alreadyCheckedUser: User) => {
          return alreadyCheckedUser.user_id === user.user_id;
        });
        let userListEntry = new UserListEntry(user);
        userListEntry.checked = isChecked;
        this.allUsers.push(userListEntry);
      });
      console.log(this.allUsers);
      this.loaded = true;
      // this.allUsers = usersData;
    }, error => {
      console.error(error);
      alert("Could not get users from server");
    });
  }

  onClickClose(): void {
    this.dialogRef.close();
  }

  onClickDone(): void {
    this.dialogRef.close(this.userTableComponent.userList);
  }
}
