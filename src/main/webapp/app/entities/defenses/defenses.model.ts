import { IShips } from 'app/entities/ships/ships.model';
import { Defense } from 'app/entities/enumerations/defense.model';

export interface IDefenses {
  id: number;
  defense?: keyof typeof Defense | null;
  dShipId?: number | null;
  battery?: number | null;
  count?: number | null;
  mass?: number | null;
  cost?: number | null;
  armored?: boolean | null;
  ships?: IShips | null;
}

export type NewDefenses = Omit<IDefenses, 'id'> & { id: null };
