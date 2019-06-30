import {Utils} from "./utils";
export module API {
   export class Base {
    protected static readonly BASE_URL = "http://localhost:58638";
    static readonly TOKEN = "http://localhost:58638/token";
   }
  // public static readonly FILE_UPLOAD_URL = URL.BASE_URL + "/files";

  export class Build extends Base {
    private static readonly URL = Base.BASE_URL + "/Build";
    static readonly getAllBuilds = Build.URL + "/GetAllBuild";
    static readonly createBuild = Build.URL + "/CreateBuild";

    static getBuild(qrcode: string) {
      return Build.URL + "/GetBuild?qrcode=" + qrcode;
    }

    static updateBuild(qrcode: string) {
      return Build.URL + "/UpdateBuild?qrcode=" + qrcode;
    }
  }
  export class Files extends Base {
    private static readonly URL = "https://s3.eu-central-1.amazonaws.com/swereabucket/";

    static getS3Path(fileName: string): string {
      let suffix = Utils.getSuffix(fileName);

      return Files.URL + suffix + "/" + fileName; // TODO: won't work for gom files
    }
  }

  export class Account extends Base {
    private static readonly URL = Base.BASE_URL + "/Account";

    static readonly REGISTER = Account.URL + "/Register";

    static getMyCall(): string {
      return Account.URL + "/MyCall";
    }
  }

  export class PrintingData extends Base {
    private static readonly URL = Base.BASE_URL + "/PrintingInfo/GetPrintingInfo";
    static Create = Base.BASE_URL + "/PrintingInfo/CreatePrintingInfo";
    // ByPartQRCode
    // private readonly GET = "";
    static getQrcode(qrcode: string): string {
      return PrintingData.URL + "?qrcode=" + qrcode;
    }
  }

  export class Parameters extends Base {
    private static readonly URL = Base.BASE_URL + "/Parameters";
    static readonly Register = Parameters.URL + "/PostConstantParameters";
    static readonly GetAllParameters = Parameters.URL + "/GetAllParameters";
    static getParameters(id: number) {
      return Parameters.URL + "/GetParameters?id=" + id;
    }
    static updateParameters(id: number) {
      return Parameters.URL + "/PutConstantParameters?id=" + id;
    }
  }
  export class Parameter extends Base {
    private static readonly URL = Base.BASE_URL + "/Parameter";
    static getParameter(id: number) {
      return Parameter.URL + "/GetVariableParameters?id=" + id;
    }
  }

}
