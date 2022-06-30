import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PatronEditorService } from '../service/patron-editor.service';
import { IPatronEditor, PatronEditor } from '../patron-editor.model';

import { PatronEditorUpdateComponent } from './patron-editor-update.component';

describe('PatronEditor Management Update Component', () => {
  let comp: PatronEditorUpdateComponent;
  let fixture: ComponentFixture<PatronEditorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patronEditorService: PatronEditorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PatronEditorUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PatronEditorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatronEditorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patronEditorService = TestBed.inject(PatronEditorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const patronEditor: IPatronEditor = { id: 456 };

      activatedRoute.data = of({ patronEditor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(patronEditor));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatronEditor>>();
      const patronEditor = { id: 123 };
      jest.spyOn(patronEditorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patronEditor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patronEditor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(patronEditorService.update).toHaveBeenCalledWith(patronEditor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatronEditor>>();
      const patronEditor = new PatronEditor();
      jest.spyOn(patronEditorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patronEditor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patronEditor }));
      saveSubject.complete();

      // THEN
      expect(patronEditorService.create).toHaveBeenCalledWith(patronEditor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PatronEditor>>();
      const patronEditor = { id: 123 };
      jest.spyOn(patronEditorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patronEditor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patronEditorService.update).toHaveBeenCalledWith(patronEditor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
