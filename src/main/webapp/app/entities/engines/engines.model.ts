import { IShips } from 'app/entities/ships/ships.model';
import { Engine } from 'app/entities/enumerations/engine.model';
import { TL } from 'app/entities/enumerations/tl.model';

export interface IEngines {
  id: number;
  eShipId?: number | null;
  engine?: keyof typeof Engine | null;
  mass?: number | null;
  cost?: number | null;
  tl?: keyof typeof TL | null;
  ships?: IShips | null;
}

export type NewEngines = Omit<IEngines, 'id'> & { id: null };
