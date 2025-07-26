import { IWeapons, NewWeapons } from './weapons.model';

export const sampleWithRequiredData: IWeapons = {
  id: 24145,
  weapon: 'Missile',
  wShipId: 29467,
  battery: 5037,
  count: 23749,
  mass: 26259.42,
  cost: 22775.75,
  armored: false,
};

export const sampleWithPartialData: IWeapons = {
  id: 1667,
  weapon: 'Laser',
  wShipId: 12891,
  battery: 1083,
  count: 29882,
  mass: 19342.94,
  cost: 27496.11,
  armored: true,
};

export const sampleWithFullData: IWeapons = {
  id: 12676,
  weapon: 'Triple',
  wShipId: 16997,
  battery: 548,
  count: 10544,
  mass: 11005.41,
  cost: 9602.77,
  armored: true,
};

export const sampleWithNewData: NewWeapons = {
  weapon: 'Missile',
  wShipId: 19276,
  battery: 8630,
  count: 32343,
  mass: 27948.05,
  cost: 1003.43,
  armored: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
