export class Magic {
  FileName: string;
  MagicScreenshot: string;
  BuildId: string;
  MagicID: string;

  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }
}
