import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPatron, Patron } from '../patron.model';
import { PatronService } from '../service/patron.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPatronEditor } from 'app/entities/patron-editor/patron-editor.model';
import { PatronEditorService } from 'app/entities/patron-editor/service/patron-editor.service';
import { PatronType } from 'app/entities/enumerations/patron-type.model';
import { Category } from 'app/entities/enumerations/category.model';

@Component({
  selector: 'jhi-patron-update',
  templateUrl: './patron-update.component.html',
})
export class PatronUpdateComponent implements OnInit {
  isSaving = false;
  patronTypeValues = Object.keys(PatronType);
  categoryValues = Object.keys(Category);

  patronEditorsSharedCollection: IPatronEditor[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    ref: [],
    type: [],
    category: [],
    sizeMin: [],
    sizeMax: [],
    buyDate: [],
    image: [],
    imageContentType: [],
    from: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected patronService: PatronService,
    protected patronEditorService: PatronEditorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patron }) => {
      this.updateForm(patron);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('coutureApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patron = this.createFromForm();
    if (patron.id !== undefined) {
      this.subscribeToSaveResponse(this.patronService.update(patron));
    } else {
      this.subscribeToSaveResponse(this.patronService.create(patron));
    }
  }

  trackPatronEditorById(_index: number, item: IPatronEditor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatron>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(patron: IPatron): void {
    this.editForm.patchValue({
      id: patron.id,
      name: patron.name,
      ref: patron.ref,
      type: patron.type,
      category: patron.category,
      sizeMin: patron.sizeMin,
      sizeMax: patron.sizeMax,
      buyDate: patron.buyDate,
      image: patron.image,
      imageContentType: patron.imageContentType,
      from: patron.from,
    });

    this.patronEditorsSharedCollection = this.patronEditorService.addPatronEditorToCollectionIfMissing(
      this.patronEditorsSharedCollection,
      patron.from
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patronEditorService
      .query()
      .pipe(map((res: HttpResponse<IPatronEditor[]>) => res.body ?? []))
      .pipe(
        map((patronEditors: IPatronEditor[]) =>
          this.patronEditorService.addPatronEditorToCollectionIfMissing(patronEditors, this.editForm.get('from')!.value)
        )
      )
      .subscribe((patronEditors: IPatronEditor[]) => (this.patronEditorsSharedCollection = patronEditors));
  }

  protected createFromForm(): IPatron {
    return {
      ...new Patron(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      ref: this.editForm.get(['ref'])!.value,
      type: this.editForm.get(['type'])!.value,
      category: this.editForm.get(['category'])!.value,
      sizeMin: this.editForm.get(['sizeMin'])!.value,
      sizeMax: this.editForm.get(['sizeMax'])!.value,
      buyDate: this.editForm.get(['buyDate'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      from: this.editForm.get(['from'])!.value,
    };
  }
}
