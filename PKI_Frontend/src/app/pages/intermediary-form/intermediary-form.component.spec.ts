import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntermediaryFormComponent } from './intermediary-form.component';

describe('IntermediaryFormComponent', () => {
  let component: IntermediaryFormComponent;
  let fixture: ComponentFixture<IntermediaryFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntermediaryFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IntermediaryFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
