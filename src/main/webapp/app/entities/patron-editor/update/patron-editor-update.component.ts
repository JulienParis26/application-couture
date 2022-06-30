import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPatronEditor, PatronEditor } from '../patron-editor.model';
import { PatronEditorService } from '../service/patron-editor.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Editors } from 'app/entities/enumerations/editors.model';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-patron-editor-update',
  templateUrl: './patron-editor-update.component.html',
})
export class PatronEditorUpdateComponent implements OnInit {
  isSaving = false;
  editorsValues = Object.keys(Editors);
  languageValues = Object.keys(Language);

  editForm = this.fb.group({
    id: [],
    name: [],
    printDate: [],
    number: [],
    editor: [],
    language: [],
    price: [],
    image: [],
    imageContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected patronEditorService: PatronEditorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patronEditor }) => {
      this.updateForm(patronEditor);
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
    const patronEditor = this.createFromForm();
    if (patronEditor.id !== undefined) {
      this.subscribeToSaveResponse(this.patronEditorService.update(patronEditor));
    } else {
      this.subscribeToSaveResponse(this.patronEditorService.create(patronEditor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatronEditor>>): void {
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

  protected updateForm(patronEditor: IPatronEditor): void {
    this.editForm.patchValue({
      id: patronEditor.id,
      name: patronEditor.name,
      printDate: patronEditor.printDate,
      number: patronEditor.number,
      editor: patronEditor.editor,
      language: patronEditor.language,
      price: patronEditor.price,
      image: patronEditor.image,
      imageContentType: patronEditor.imageContentType,
    });
  }

  protected createFromForm(): IPatronEditor {
    return {
      ...new PatronEditor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      printDate: this.editForm.get(['printDate'])!.value,
      number: this.editForm.get(['number'])!.value,
      editor: this.editForm.get(['editor'])!.value,
      language: this.editForm.get(['language'])!.value,
      price: this.editForm.get(['price'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
    };
  }
}
