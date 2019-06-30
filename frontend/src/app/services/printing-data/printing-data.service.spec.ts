import { TestBed, inject } from '@angular/core/testing';

import { PrintingDataService } from './printing-data.service';

describe('PrintingDataService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PrintingDataService]
    });
  });

  it('should be created', inject([PrintingDataService], (service: PrintingDataService) => {
    expect(service).toBeTruthy();
  }));
});
