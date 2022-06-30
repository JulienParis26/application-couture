import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PatronService } from '../service/patron.service';
import { IPatron, Patron } from '../patron.model';
import { IPatronEditor } from 'app/entities/patron-editor/patron-editor.model';
import { PatronEditorService } from 'app/entities/patron-editor/service/patron-editor.service';

import { PatronUpdateComponent } from './patron-update.component';

describe('Patron Management Update Component', () => {
  let comp: PatronUpdateComponent;
  let fixture: ComponentFixture<PatronUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patronService: PatronService;
  let patronEditorService: PatronEditorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PatronUpdateComponent],
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
      .overrideTemplate(PatronUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatronUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patronService = TestBed.inject(PatronService);
    patronEditorService = TestBed.inject(PatronEditorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PatronEditor query and add missing value', () => {
      const patron: IPatron = { id: 456 };
      const from: IPatronEditor = { id: 33083 };
      patron.from = from;

      const patronEditorCollection: IPatronEditor[] = [{ id: 58875 }];
      jest.spyOn(patronEditorService, 'query').mockReturnValue(of(new HttpResponse({ body: patronEditorCollection })));
      const additionalPatronEditors = [from];
      const expectedCollection: IPatronEditor[] = [...additionalPatronEditors, ...patronEditorCollection];
      jest.spyOn(patronEditorService, 'addPatronEditorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ patron });
      comp.ngOnInit();

      expect(patronEditorService.query).toHaveBeenCalled();
      expect(patronEditorService.addPatronEditorToCollectionIfMissing).toHaveBeenCalledWith(
        patronEditorCollection,
        ...additionalPatronEditors
      );
      expect(comp.patronEditorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const patron: IPatron = { id: 456 };
      const from: IPatronEditor = { id: 3557 };
      patron.from = from;

      activatedRoute.data = of({ patron });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(patron));
      expect(comp.patronEditorsSharedCollection).toContain(from);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Patron>>();
      const patron = { id: 123 };
      jest.spyOn(patronService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patron });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patron }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(patronService.update).toHaveBeenCalledWith(patron);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Patron>>();
      const patron = new Patron();
      jest.spyOn(patronService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patron });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: patron }));
      saveSubject.complete();

      // THEN
      expect(patronService.create).toHaveBeenCalledWith(patron);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Patron>>();
      const patron = { id: 123 };
      jest.spyOn(patronService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ patron });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patronService.update).toHaveBeenCalledWith(patron);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPatronEditorById', () => {
      it('Should return tracked PatronEditor primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPatronEditorById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
