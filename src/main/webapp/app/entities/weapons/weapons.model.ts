import { IShips } from 'app/entities/ships/ships.model';
import { Weapon } from 'app/entities/enumerations/weapon.model';

export interface IWeapons {
  id: number;
  weapon?: keyof typeof Weapon | null;
  wShipId?: number | null;
  battery?: number | null;
  count?: number | null;
  mass?: number | null;
  cost?: number | null;
  armored?: boolean | null;
  ships?: IShips | null;
}

export type NewWeapons = Omit<IWeapons, 'id'> & { id: null };
