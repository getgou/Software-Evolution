export class Part {
  PartID: number;
  PrtFileName: string;
  StlFileName: string;
  GomPart: string[] = [];
  magicsIDs: string[] = [];
  MagicID: string;
  QR_code: string;

  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }
}
