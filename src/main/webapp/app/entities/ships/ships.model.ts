import { IUser } from 'app/entities/user/user.model';
import { TL } from 'app/entities/enumerations/tl.model';
import { Config } from 'app/entities/enumerations/config.model';

export interface IShips {
  id: number;
  shipId?: number | null;
  userId?: number | null;
  tl?: keyof typeof TL | null;
  tonnage?: number | null;
  conf?: keyof typeof Config | null;
  code?: string | null;
  hull?: number | null;
  structure?: number | null;
  sections?: number | null;
  capital?: boolean | null;
  military?: boolean | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewShips = Omit<IShips, 'id'> & { id: null };
