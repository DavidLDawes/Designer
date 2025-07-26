import { IShips } from 'app/entities/ships/ships.model';
import { Fitting } from 'app/entities/enumerations/fitting.model';

export interface IFittings {
  id: number;
  fitting?: keyof typeof Fitting | null;
  fShipId?: number | null;
  count?: number | null;
  mass?: number | null;
  cost?: number | null;
  armored?: boolean | null;
  ships?: IShips | null;
}

export type NewFittings = Omit<IFittings, 'id'> & { id: null };
