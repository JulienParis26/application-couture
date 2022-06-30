import dayjs from 'dayjs/esm';
import { IPatronEditor } from 'app/entities/patron-editor/patron-editor.model';
import { IProject } from 'app/entities/project/project.model';
import { PatronType } from 'app/entities/enumerations/patron-type.model';
import { Category } from 'app/entities/enumerations/category.model';

export interface IPatron {
  id?: number;
  name?: string | null;
  ref?: string | null;
  type?: PatronType | null;
  category?: Category | null;
  sizeMin?: number | null;
  sizeMax?: number | null;
  buyDate?: dayjs.Dayjs | null;
  imageContentType?: string | null;
  image?: string | null;
  from?: IPatronEditor | null;
  projetcs?: IProject[] | null;
}

export class Patron implements IPatron {
  constructor(
    public id?: number,
    public name?: string | null,
    public ref?: string | null,
    public type?: PatronType | null,
    public category?: Category | null,
    public sizeMin?: number | null,
    public sizeMax?: number | null,
    public buyDate?: dayjs.Dayjs | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public from?: IPatronEditor | null,
    public projetcs?: IProject[] | null
  ) {}
}

export function getPatronIdentifier(patron: IPatron): number | undefined {
  return patron.id;
}
