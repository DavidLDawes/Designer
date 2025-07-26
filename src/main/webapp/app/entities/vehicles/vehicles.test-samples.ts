import { IVehicles, NewVehicles } from './vehicles.model';

export const sampleWithRequiredData: IVehicles = {
  id: 28087,
  vehicle: 'ATV',
  vShipId: 16337,
  mass: 14230.13,
  cost: 24409.39,
  armored: true,
  repairBay: true,
};

export const sampleWithPartialData: IVehicles = {
  id: 26928,
  vehicle: 'Modular',
  vShipId: 16665,
  mass: 15833.05,
  cost: 458.09,
  armored: false,
  repairBay: true,
};

export const sampleWithFullData: IVehicles = {
  id: 25357,
  vehicle: 'Heavy',
  vShipId: 11893,
  mass: 29792.39,
  cost: 10780.8,
  armored: true,
  repairBay: true,
};

export const sampleWithNewData: NewVehicles = {
  vehicle: 'Ships',
  vShipId: 23960,
  mass: 4438.13,
  cost: 7323.81,
  armored: true,
  repairBay: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
