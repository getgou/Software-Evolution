export class Utils {
  static isValidFileFormat(suffix: string) {
    switch (suffix) {
      case 'png':
      case 'jpg':
      case 'jpeg':
      case 'gif':
      case 'magics':
      case 'stl':
      case 'prt':
      case 'slm':
        return true;
      default:
        return false;
    }
  }

  static removeSuffix(fileName: string): string {
    if (fileName != null) {
      return fileName.slice(0, fileName.lastIndexOf("."));
    } else {
      return "";
    }
  }

  static getSuffix(fileName: string): string {
    if (fileName != null) {
      let lastDotIndex = fileName.lastIndexOf(".");
      if (lastDotIndex > -1) {
        return fileName.substring(lastDotIndex + 1);
      }
    }
    return "";
  }

  static getPathFromSuffix(suffix: string) {
    switch (suffix) {
      case 'magics':
        return 'magic';
      case 'png':
      case 'jpg':
      case 'jpeg':
      case 'gif':
        return 'magicscreenshot';
      default:
        return suffix;
    }
  }
}
