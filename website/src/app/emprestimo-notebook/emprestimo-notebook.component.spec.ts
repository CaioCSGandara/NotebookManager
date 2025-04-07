import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmprestimoNotebookComponent } from './emprestimo-notebook.component';

describe('EmprestimoNotebookComponent', () => {
  let component: EmprestimoNotebookComponent;
  let fixture: ComponentFixture<EmprestimoNotebookComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmprestimoNotebookComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmprestimoNotebookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
