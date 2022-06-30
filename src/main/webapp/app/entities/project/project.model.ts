import dayjs from 'dayjs/esm';
import { IPatron } from 'app/entities/patron/patron.model';
import { ITissu } from 'app/entities/tissu/tissu.model';

export interface IProject {
  id?: number;
  name?: string | null;
  ref?: string | null;
  creationDate?: dayjs.Dayjs | null;
  image1ContentType?: string | null;
  image1?: string | null;
  image2ContentType?: string | null;
  image2?: string | null;
  image3ContentType?: string | null;
  image3?: string | null;
  image4ContentType?: string | null;
  image4?: string | null;
  patron?: IPatron | null;
  matieres?: ITissu[] | null;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string | null,
    public ref?: string | null,
    public creationDate?: dayjs.Dayjs | null,
    public image1ContentType?: string | null,
    public image1?: string | null,
    public image2ContentType?: string | null,
    public image2?: string | null,
    public image3ContentType?: string | null,
    public image3?: string | null,
    public image4ContentType?: string | null,
    public image4?: string | null,
    public patron?: IPatron | null,
    public matieres?: ITissu[] | null
  ) {}
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
