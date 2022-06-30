import dayjs from 'dayjs/esm';
import { Editors } from 'app/entities/enumerations/editors.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IPatronEditor {
  id?: number;
  name?: string | null;
  printDate?: dayjs.Dayjs | null;
  number?: string | null;
  editor?: Editors | null;
  language?: Language | null;
  price?: number | null;
  imageContentType?: string | null;
  image?: string | null;
}

export class PatronEditor implements IPatronEditor {
  constructor(
    public id?: number,
    public name?: string | null,
    public printDate?: dayjs.Dayjs | null,
    public number?: string | null,
    public editor?: Editors | null,
    public language?: Language | null,
    public price?: number | null,
    public imageContentType?: string | null,
    public image?: string | null
  ) {}
}

export function getPatronEditorIdentifier(patronEditor: IPatronEditor): number | undefined {
  return patronEditor.id;
}
