import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { IngredientEditComponent } from './ingredient-edit.component';
import { IngredientService } from '../ingredient.service';

describe('IngredientEditComponent', () => {
  let component: IngredientEditComponent;
  let fixture: ComponentFixture<IngredientEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [IngredientEditComponent],
      imports: [FormsModule, HttpClientTestingModule, RouterTestingModule],
      providers: [IngredientService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IngredientEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
