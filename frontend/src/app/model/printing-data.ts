export class PrintingData {
  printingInfoID: number;
  buildStatus: string;
  startTime: Date;
  endTime: Date;
  printingDate: Date;
  operator: string;
  typeOfmachine: string;
  powderWeightStart: number;
  powderweightEnd: number;
  weightPowderWaste: number;
  powerUsed: number;
  platformMaterial: string;
  platformWeight: number;
  printedTime: Date;
  powderCondition: string;
  numberLayers: number;
  dpcFactor: number;
  minExposureTime: Date;
  comments: string;
  buildId: number;

  constructor(values: Object = {}) {
    if (!values) {
      return null;
    }
    Object.assign(this, values);
  }
}
