import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISeller, Seller } from '../seller.model';
import { SellerService } from '../service/seller.service';
import { ITissu } from 'app/entities/tissu/tissu.model';
import { TissuService } from 'app/entities/tissu/service/tissu.service';

@Component({
  selector: 'jhi-seller-update',
  templateUrl: './seller-update.component.html',
})
export class SellerUpdateComponent implements OnInit {
  isSaving = false;

  tissusSharedCollection: ITissu[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    webSite: [],
    tissus: [],
  });

  constructor(
    protected sellerService: SellerService,
    protected tissuService: TissuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seller }) => {
      this.updateForm(seller);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seller = this.createFromForm();
    if (seller.id !== undefined) {
      this.subscribeToSaveResponse(this.sellerService.update(seller));
    } else {
      this.subscribeToSaveResponse(this.sellerService.create(seller));
    }
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeller>>): void {
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

  protected updateForm(seller: ISeller): void {
    this.editForm.patchValue({
      id: seller.id,
      name: seller.name,
      webSite: seller.webSite,
      tissus: seller.tissus,
    });

    this.tissusSharedCollection = this.tissuService.addTissuToCollectionIfMissing(this.tissusSharedCollection, ...(seller.tissus ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.tissuService
      .query()
      .pipe(map((res: HttpResponse<ITissu[]>) => res.body ?? []))
      .pipe(
        map((tissus: ITissu[]) => this.tissuService.addTissuToCollectionIfMissing(tissus, ...(this.editForm.get('tissus')!.value ?? [])))
      )
      .subscribe((tissus: ITissu[]) => (this.tissusSharedCollection = tissus));
  }

  protected createFromForm(): ISeller {
    return {
      ...new Seller(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      webSite: this.editForm.get(['webSite'])!.value,
      tissus: this.editForm.get(['tissus'])!.value,
    };
  }
}
