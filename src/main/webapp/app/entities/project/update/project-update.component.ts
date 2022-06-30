import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProject, Project } from '../project.model';
import { ProjectService } from '../service/project.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPatron } from 'app/entities/patron/patron.model';
import { PatronService } from 'app/entities/patron/service/patron.service';
import { ITissu } from 'app/entities/tissu/tissu.model';
import { TissuService } from 'app/entities/tissu/service/tissu.service';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html',
})
export class ProjectUpdateComponent implements OnInit {
  isSaving = false;

  patronsSharedCollection: IPatron[] = [];
  tissusSharedCollection: ITissu[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    ref: [],
    creationDate: [],
    image1: [],
    image1ContentType: [],
    image2: [],
    image2ContentType: [],
    image3: [],
    image3ContentType: [],
    image4: [],
    image4ContentType: [],
    patron: [],
    matieres: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected projectService: ProjectService,
    protected patronService: PatronService,
    protected tissuService: TissuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.updateForm(project);

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
    const project = this.createFromForm();
    if (project.id !== undefined) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  trackPatronById(_index: number, item: IPatron): number {
    return item.id!;
  }

  trackTissuById(_index: number, item: ITissu): number {
    return item.id!;
  }

  getSelectedTissu(option: ITissu, selectedVals?: ITissu[]): ITissu {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>): void {
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

  protected updateForm(project: IProject): void {
    this.editForm.patchValue({
      id: project.id,
      name: project.name,
      ref: project.ref,
      creationDate: project.creationDate,
      image1: project.image1,
      image1ContentType: project.image1ContentType,
      image2: project.image2,
      image2ContentType: project.image2ContentType,
      image3: project.image3,
      image3ContentType: project.image3ContentType,
      image4: project.image4,
      image4ContentType: project.image4ContentType,
      patron: project.patron,
      matieres: project.matieres,
    });

    this.patronsSharedCollection = this.patronService.addPatronToCollectionIfMissing(this.patronsSharedCollection, project.patron);
    this.tissusSharedCollection = this.tissuService.addTissuToCollectionIfMissing(this.tissusSharedCollection, ...(project.matieres ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.patronService
      .query()
      .pipe(map((res: HttpResponse<IPatron[]>) => res.body ?? []))
      .pipe(map((patrons: IPatron[]) => this.patronService.addPatronToCollectionIfMissing(patrons, this.editForm.get('patron')!.value)))
      .subscribe((patrons: IPatron[]) => (this.patronsSharedCollection = patrons));

    this.tissuService
      .query()
      .pipe(map((res: HttpResponse<ITissu[]>) => res.body ?? []))
      .pipe(
        map((tissus: ITissu[]) => this.tissuService.addTissuToCollectionIfMissing(tissus, ...(this.editForm.get('matieres')!.value ?? [])))
      )
      .subscribe((tissus: ITissu[]) => (this.tissusSharedCollection = tissus));
  }

  protected createFromForm(): IProject {
    return {
      ...new Project(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      ref: this.editForm.get(['ref'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      image1ContentType: this.editForm.get(['image1ContentType'])!.value,
      image1: this.editForm.get(['image1'])!.value,
      image2ContentType: this.editForm.get(['image2ContentType'])!.value,
      image2: this.editForm.get(['image2'])!.value,
      image3ContentType: this.editForm.get(['image3ContentType'])!.value,
      image3: this.editForm.get(['image3'])!.value,
      image4ContentType: this.editForm.get(['image4ContentType'])!.value,
      image4: this.editForm.get(['image4'])!.value,
      patron: this.editForm.get(['patron'])!.value,
      matieres: this.editForm.get(['matieres'])!.value,
    };
  }
}
