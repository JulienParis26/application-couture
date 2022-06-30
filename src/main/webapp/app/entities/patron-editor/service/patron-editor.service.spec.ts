import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Editors } from 'app/entities/enumerations/editors.model';
import { Language } from 'app/entities/enumerations/language.model';
import { IPatronEditor, PatronEditor } from '../patron-editor.model';

import { PatronEditorService } from './patron-editor.service';

describe('PatronEditor Service', () => {
  let service: PatronEditorService;
  let httpMock: HttpTestingController;
  let elemDefault: IPatronEditor;
  let expectedResult: IPatronEditor | IPatronEditor[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PatronEditorService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      printDate: currentDate,
      number: 'AAAAAAA',
      editor: Editors.BURDA,
      language: Language.FRENCH,
      price: 0,
      imageContentType: 'image/png',
      image: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          printDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PatronEditor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          printDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          printDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PatronEditor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PatronEditor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          printDate: currentDate.format(DATE_FORMAT),
          number: 'BBBBBB',
          editor: 'BBBBBB',
          language: 'BBBBBB',
          price: 1,
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          printDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PatronEditor', () => {
      const patchObject = Object.assign({}, new PatronEditor());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          printDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PatronEditor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          printDate: currentDate.format(DATE_FORMAT),
          number: 'BBBBBB',
          editor: 'BBBBBB',
          language: 'BBBBBB',
          price: 1,
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          printDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PatronEditor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPatronEditorToCollectionIfMissing', () => {
      it('should add a PatronEditor to an empty array', () => {
        const patronEditor: IPatronEditor = { id: 123 };
        expectedResult = service.addPatronEditorToCollectionIfMissing([], patronEditor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patronEditor);
      });

      it('should not add a PatronEditor to an array that contains it', () => {
        const patronEditor: IPatronEditor = { id: 123 };
        const patronEditorCollection: IPatronEditor[] = [
          {
            ...patronEditor,
          },
          { id: 456 },
        ];
        expectedResult = service.addPatronEditorToCollectionIfMissing(patronEditorCollection, patronEditor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PatronEditor to an array that doesn't contain it", () => {
        const patronEditor: IPatronEditor = { id: 123 };
        const patronEditorCollection: IPatronEditor[] = [{ id: 456 }];
        expectedResult = service.addPatronEditorToCollectionIfMissing(patronEditorCollection, patronEditor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patronEditor);
      });

      it('should add only unique PatronEditor to an array', () => {
        const patronEditorArray: IPatronEditor[] = [{ id: 123 }, { id: 456 }, { id: 81688 }];
        const patronEditorCollection: IPatronEditor[] = [{ id: 123 }];
        expectedResult = service.addPatronEditorToCollectionIfMissing(patronEditorCollection, ...patronEditorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const patronEditor: IPatronEditor = { id: 123 };
        const patronEditor2: IPatronEditor = { id: 456 };
        expectedResult = service.addPatronEditorToCollectionIfMissing([], patronEditor, patronEditor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(patronEditor);
        expect(expectedResult).toContain(patronEditor2);
      });

      it('should accept null and undefined values', () => {
        const patronEditor: IPatronEditor = { id: 123 };
        expectedResult = service.addPatronEditorToCollectionIfMissing([], null, patronEditor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(patronEditor);
      });

      it('should return initial array if no PatronEditor is added', () => {
        const patronEditorCollection: IPatronEditor[] = [{ id: 123 }];
        expectedResult = service.addPatronEditorToCollectionIfMissing(patronEditorCollection, undefined, null);
        expect(expectedResult).toEqual(patronEditorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
