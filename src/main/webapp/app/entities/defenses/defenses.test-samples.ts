import { IDefenses, NewDefenses } from './defenses.model';

export const sampleWithRequiredData: IDefenses = {
  id: 9278,
  defense: 'Ablative',
  dShipId: 23339,
  battery: 5226,
  count: 10813,
  mass: 19455.61,
  cost: 4307.11,
  armored: true,
};

export const sampleWithPartialData: IDefenses = {
  id: 22263,
  defense: 'Triple',
  dShipId: 1017,
  battery: 1766,
  count: 1602,
  mass: 960.06,
  cost: 17384.11,
  armored: true,
};

export const sampleWithFullData: IDefenses = {
  id: 31125,
  defense: 'Coating',
  dShipId: 16978,
  battery: 7650,
  count: 1865,
  mass: 3194.9,
  cost: 31479.71,
  armored: false,
};

export const sampleWithNewData: NewDefenses = {
  defense: 'Defense',
  dShipId: 3053,
  battery: 17984,
  count: 12671,
  mass: 4272,
  cost: 4411.63,
  armored: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
