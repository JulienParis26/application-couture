import dayjs from 'dayjs/esm';
import { ISeller } from 'app/entities/seller/seller.model';
import { IProject } from 'app/entities/project/project.model';
import { TissuType } from 'app/entities/enumerations/tissu-type.model';

export interface ITissu {
  id?: number;
  name?: string | null;
  ref?: string | null;
  color?: string | null;
  buySize?: string | null;
  type?: TissuType | null;
  buyDate?: dayjs.Dayjs | null;
  imageContentType?: string | null;
  image?: string | null;
  sellers?: ISeller[] | null;
  projects?: IProject[] | null;
}

export class Tissu implements ITissu {
  constructor(
    public id?: number,
    public name?: string | null,
    public ref?: string | null,
    public color?: string | null,
    public buySize?: string | null,
    public type?: TissuType | null,
    public buyDate?: dayjs.Dayjs | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public sellers?: ISeller[] | null,
    public projects?: IProject[] | null
  ) {}
}

export function getTissuIdentifier(tissu: ITissu): number | undefined {
  return tissu.id;
}
