import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TissuType } from 'app/entities/enumerations/tissu-type.model';
import { ITissu, Tissu } from '../tissu.model';

import { TissuService } from './tissu.service';

describe('Tissu Service', () => {
  let service: TissuService;
  let httpMock: HttpTestingController;
  let elemDefault: ITissu;
  let expectedResult: ITissu | ITissu[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TissuService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      ref: 'AAAAAAA',
      color: 'AAAAAAA',
      buySize: 'AAAAAAA',
      type: TissuType.JERSEY,
      buyDate: currentDate,
      imageContentType: 'image/png',
      image: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          buyDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Tissu', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          buyDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          buyDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Tissu()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tissu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          ref: 'BBBBBB',
          color: 'BBBBBB',
          buySize: 'BBBBBB',
          type: 'BBBBBB',
          buyDate: currentDate.format(DATE_FORMAT),
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          buyDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tissu', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          ref: 'BBBBBB',
          buyDate: currentDate.format(DATE_FORMAT),
          image: 'BBBBBB',
        },
        new Tissu()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          buyDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tissu', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          ref: 'BBBBBB',
          color: 'BBBBBB',
          buySize: 'BBBBBB',
          type: 'BBBBBB',
          buyDate: currentDate.format(DATE_FORMAT),
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          buyDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Tissu', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTissuToCollectionIfMissing', () => {
      it('should add a Tissu to an empty array', () => {
        const tissu: ITissu = { id: 123 };
        expectedResult = service.addTissuToCollectionIfMissing([], tissu);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tissu);
      });

      it('should not add a Tissu to an array that contains it', () => {
        const tissu: ITissu = { id: 123 };
        const tissuCollection: ITissu[] = [
          {
            ...tissu,
          },
          { id: 456 },
        ];
        expectedResult = service.addTissuToCollectionIfMissing(tissuCollection, tissu);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tissu to an array that doesn't contain it", () => {
        const tissu: ITissu = { id: 123 };
        const tissuCollection: ITissu[] = [{ id: 456 }];
        expectedResult = service.addTissuToCollectionIfMissing(tissuCollection, tissu);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tissu);
      });

      it('should add only unique Tissu to an array', () => {
        const tissuArray: ITissu[] = [{ id: 123 }, { id: 456 }, { id: 46162 }];
        const tissuCollection: ITissu[] = [{ id: 123 }];
        expectedResult = service.addTissuToCollectionIfMissing(tissuCollection, ...tissuArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tissu: ITissu = { id: 123 };
        const tissu2: ITissu = { id: 456 };
        expectedResult = service.addTissuToCollectionIfMissing([], tissu, tissu2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tissu);
        expect(expectedResult).toContain(tissu2);
      });

      it('should accept null and undefined values', () => {
        const tissu: ITissu = { id: 123 };
        expectedResult = service.addTissuToCollectionIfMissing([], null, tissu, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tissu);
      });

      it('should return initial array if no Tissu is added', () => {
        const tissuCollection: ITissu[] = [{ id: 123 }];
        expectedResult = service.addTissuToCollectionIfMissing(tissuCollection, undefined, null);
        expect(expectedResult).toEqual(tissuCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
