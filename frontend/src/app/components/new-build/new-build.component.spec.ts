import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBuildComponent } from './new-build.component';

describe('NewBuildComponent', () => {
  let component: NewBuildComponent;
  let fixture: ComponentFixture<NewBuildComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewBuildComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewBuildComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
