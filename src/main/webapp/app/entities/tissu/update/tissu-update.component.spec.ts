import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TissuService } from '../service/tissu.service';
import { ITissu, Tissu } from '../tissu.model';

import { TissuUpdateComponent } from './tissu-update.component';

describe('Tissu Management Update Component', () => {
  let comp: TissuUpdateComponent;
  let fixture: ComponentFixture<TissuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tissuService: TissuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TissuUpdateComponent],
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
      .overrideTemplate(TissuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TissuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tissuService = TestBed.inject(TissuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tissu: ITissu = { id: 456 };

      activatedRoute.data = of({ tissu });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tissu));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tissu>>();
      const tissu = { id: 123 };
      jest.spyOn(tissuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tissu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tissu }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tissuService.update).toHaveBeenCalledWith(tissu);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tissu>>();
      const tissu = new Tissu();
      jest.spyOn(tissuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tissu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tissu }));
      saveSubject.complete();

      // THEN
      expect(tissuService.create).toHaveBeenCalledWith(tissu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tissu>>();
      const tissu = { id: 123 };
      jest.spyOn(tissuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tissu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tissuService.update).toHaveBeenCalledWith(tissu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
