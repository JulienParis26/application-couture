import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITissu, Tissu } from '../tissu.model';
import { TissuService } from '../service/tissu.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { TissuType } from 'app/entities/enumerations/tissu-type.model';

@Component({
  selector: 'jhi-tissu-update',
  templateUrl: './tissu-update.component.html',
})
export class TissuUpdateComponent implements OnInit {
  isSaving = false;
  tissuTypeValues = Object.keys(TissuType);

  editForm = this.fb.group({
    id: [],
    name: [],
    ref: [],
    color: [],
    buySize: [],
    type: [],
    buyDate: [],
    image: [],
    imageContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tissuService: TissuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tissu }) => {
      this.updateForm(tissu);
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
    const tissu = this.createFromForm();
    if (tissu.id !== undefined) {
      this.subscribeToSaveResponse(this.tissuService.update(tissu));
    } else {
      this.subscribeToSaveResponse(this.tissuService.create(tissu));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITissu>>): void {
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

  protected updateForm(tissu: ITissu): void {
    this.editForm.patchValue({
      id: tissu.id,
      name: tissu.name,
      ref: tissu.ref,
      color: tissu.color,
      buySize: tissu.buySize,
      type: tissu.type,
      buyDate: tissu.buyDate,
      image: tissu.image,
      imageContentType: tissu.imageContentType,
    });
  }

  protected createFromForm(): ITissu {
    return {
      ...new Tissu(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      ref: this.editForm.get(['ref'])!.value,
      color: this.editForm.get(['color'])!.value,
      buySize: this.editForm.get(['buySize'])!.value,
      type: this.editForm.get(['type'])!.value,
      buyDate: this.editForm.get(['buyDate'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
    };
  }
}
