import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SellerService } from '../service/seller.service';
import { ISeller, Seller } from '../seller.model';
import { ITissu } from 'app/entities/tissu/tissu.model';
import { TissuService } from 'app/entities/tissu/service/tissu.service';

import { SellerUpdateComponent } from './seller-update.component';

describe('Seller Management Update Component', () => {
  let comp: SellerUpdateComponent;
  let fixture: ComponentFixture<SellerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sellerService: SellerService;
  let tissuService: TissuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SellerUpdateComponent],
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
      .overrideTemplate(SellerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SellerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sellerService = TestBed.inject(SellerService);
    tissuService = TestBed.inject(TissuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tissu query and add missing value', () => {
      const seller: ISeller = { id: 456 };
      const tissus: ITissu[] = [{ id: 9322 }];
      seller.tissus = tissus;

      const tissuCollection: ITissu[] = [{ id: 86036 }];
      jest.spyOn(tissuService, 'query').mockReturnValue(of(new HttpResponse({ body: tissuCollection })));
      const additionalTissus = [...tissus];
      const expectedCollection: ITissu[] = [...additionalTissus, ...tissuCollection];
      jest.spyOn(tissuService, 'addTissuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      expect(tissuService.query).toHaveBeenCalled();
      expect(tissuService.addTissuToCollectionIfMissing).toHaveBeenCalledWith(tissuCollection, ...additionalTissus);
      expect(comp.tissusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const seller: ISeller = { id: 456 };
      const tissus: ITissu = { id: 98469 };
      seller.tissus = [tissus];

      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(seller));
      expect(comp.tissusSharedCollection).toContain(tissus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Seller>>();
      const seller = { id: 123 };
      jest.spyOn(sellerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seller }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sellerService.update).toHaveBeenCalledWith(seller);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Seller>>();
      const seller = new Seller();
      jest.spyOn(sellerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: seller }));
      saveSubject.complete();

      // THEN
      expect(sellerService.create).toHaveBeenCalledWith(seller);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Seller>>();
      const seller = { id: 123 };
      jest.spyOn(sellerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ seller });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sellerService.update).toHaveBeenCalledWith(seller);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
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
