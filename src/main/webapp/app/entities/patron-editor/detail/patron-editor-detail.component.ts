import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatronEditor } from '../patron-editor.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-patron-editor-detail',
  templateUrl: './patron-editor-detail.component.html',
})
export class PatronEditorDetailComponent implements OnInit {
  patronEditor: IPatronEditor | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patronEditor }) => {
      this.patronEditor = patronEditor;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
