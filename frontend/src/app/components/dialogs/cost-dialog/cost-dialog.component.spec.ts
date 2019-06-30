import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CostDialogComponent } from './cost-dialog.component';

describe('CostDialogComponent', () => {
  let component: CostDialogComponent;
  let fixture: ComponentFixture<CostDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CostDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CostDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
