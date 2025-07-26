import { IFittings, NewFittings } from './fittings.model';

export const sampleWithRequiredData: IFittings = {
  id: 29159,
  fitting: 'Tubes',
  fShipId: 7643,
  count: 7996,
  mass: 10201.05,
  cost: 23485.83,
  armored: false,
};

export const sampleWithPartialData: IFittings = {
  id: 14169,
  fitting: 'Sensors',
  fShipId: 18708,
  count: 13207,
  mass: 22997.25,
  cost: 12300.67,
  armored: true,
};

export const sampleWithFullData: IFittings = {
  id: 169,
  fitting: 'Sensors',
  fShipId: 24921,
  count: 15608,
  mass: 18657.91,
  cost: 14220.44,
  armored: true,
};

export const sampleWithNewData: NewFittings = {
  fitting: 'Tubes',
  fShipId: 26588,
  count: 1006,
  mass: 15829.19,
  cost: 457.41,
  armored: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
