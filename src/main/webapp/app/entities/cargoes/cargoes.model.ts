import { IShips } from 'app/entities/ships/ships.model';
import { Cargo } from 'app/entities/enumerations/cargo.model';

export interface ICargoes {
  id: number;
  cargo?: keyof typeof Cargo | null;
  cShipId?: number | null;
  mass?: number | null;
  cost?: number | null;
  armored?: boolean | null;
  ships?: IShips | null;
}

export type NewCargoes = Omit<ICargoes, 'id'> & { id: null };
