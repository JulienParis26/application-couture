import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProjectService } from '../service/project.service';
import { IProject, Project } from '../project.model';
import { IPatron } from 'app/entities/patron/patron.model';
import { PatronService } from 'app/entities/patron/service/patron.service';
import { ITissu } from 'app/entities/tissu/tissu.model';
import { TissuService } from 'app/entities/tissu/service/tissu.service';

import { ProjectUpdateComponent } from './project-update.component';

describe('Project Management Update Component', () => {
  let comp: ProjectUpdateComponent;
  let fixture: ComponentFixture<ProjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectService: ProjectService;
  let patronService: PatronService;
  let tissuService: TissuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProjectUpdateComponent],
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
      .overrideTemplate(ProjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectService = TestBed.inject(ProjectService);
    patronService = TestBed.inject(PatronService);
    tissuService = TestBed.inject(TissuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Patron query and add missing value', () => {
      const project: IProject = { id: 456 };
      const patron: IPatron = { id: 63248 };
      project.patron = patron;

      const patronCollection: IPatron[] = [{ id: 33596 }];
      jest.spyOn(patronService, 'query').mockReturnValue(of(new HttpResponse({ body: patronCollection })));
      const additionalPatrons = [patron];
      const expectedCollection: IPatron[] = [...additionalPatrons, ...patronCollection];
      jest.spyOn(patronService, 'addPatronToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(patronService.query).toHaveBeenCalled();
      expect(patronService.addPatronToCollectionIfMissing).toHaveBeenCalledWith(patronCollection, ...additionalPatrons);
      expect(comp.patronsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tissu query and add missing value', () => {
      const project: IProject = { id: 456 };
      const matieres: ITissu[] = [{ id: 5328 }];
      project.matieres = matieres;

      const tissuCollection: ITissu[] = [{ id: 7432 }];
      jest.spyOn(tissuService, 'query').mockReturnValue(of(new HttpResponse({ body: tissuCollection })));
      const additionalTissus = [...matieres];
      const expectedCollection: ITissu[] = [...additionalTissus, ...tissuCollection];
      jest.spyOn(tissuService, 'addTissuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(tissuService.query).toHaveBeenCalled();
      expect(tissuService.addTissuToCollectionIfMissing).toHaveBeenCalledWith(tissuCollection, ...additionalTissus);
      expect(comp.tissusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const project: IProject = { id: 456 };
      const patron: IPatron = { id: 28851 };
      project.patron = patron;
      const matieres: ITissu = { id: 50824 };
      project.matieres = [matieres];

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(project));
      expect(comp.patronsSharedCollection).toContain(patron);
      expect(comp.tissusSharedCollection).toContain(matieres);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Project>>();
      const project = { id: 123 };
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectService.update).toHaveBeenCalledWith(project);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Project>>();
      const project = new Project();
      jest.spyOn(projectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(projectService.create).toHaveBeenCalledWith(project);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Project>>();
      const project = { id: 123 };
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectService.update).toHaveBeenCalledWith(project);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPatronById', () => {
      it('Should return tracked Patron primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPatronById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTissuById', () => {
      it('Should return tracked Tissu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTissuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTissu', () => {
      it('Should return option if no Tissu is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTissu(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Tissu for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTissu(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Tissu is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTissu(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
