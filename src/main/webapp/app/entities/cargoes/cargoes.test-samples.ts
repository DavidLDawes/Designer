import { ICargoes, NewCargoes } from './cargoes.model';

export const sampleWithRequiredData: ICargoes = {
  id: 32257,
  cargo: 'Storage',
  cShipId: 11208,
  mass: 27956.22,
  cost: 27595.8,
  armored: true,
};

export const sampleWithPartialData: ICargoes = {
  id: 26623,
  cargo: 'Data',
  cShipId: 22362,
  mass: 18139.08,
  cost: 7203.85,
  armored: true,
};

export const sampleWithFullData: ICargoes = {
  id: 16071,
  cargo: 'Cargo',
  cShipId: 529,
  mass: 10991.15,
  cost: 20723,
  armored: false,
};

export const sampleWithNewData: NewCargoes = {
  cargo: 'Cold',
  cShipId: 19850,
  mass: 32163.46,
  cost: 28268.28,
  armored: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
