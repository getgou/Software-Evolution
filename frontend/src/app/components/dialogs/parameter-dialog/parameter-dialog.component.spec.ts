import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParameterDialogComponent } from './parameter-dialog.component';

describe('ParameterDialogComponent', () => {
  let component: ParameterDialogComponent;
  let fixture: ComponentFixture<ParameterDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParameterDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParameterDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
