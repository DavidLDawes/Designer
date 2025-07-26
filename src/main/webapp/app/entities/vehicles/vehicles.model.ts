import { IShips } from 'app/entities/ships/ships.model';
import { Vehicle } from 'app/entities/enumerations/vehicle.model';

export interface IVehicles {
  id: number;
  vehicle?: keyof typeof Vehicle | null;
  vShipId?: number | null;
  mass?: number | null;
  cost?: number | null;
  armored?: boolean | null;
  repairBay?: boolean | null;
  ships?: IShips | null;
}

export type NewVehicles = Omit<IVehicles, 'id'> & { id: null };
