import { IEngines, NewEngines } from './engines.model';

export const sampleWithRequiredData: IEngines = {
  id: 7959,
  eShipId: 30517,
  engine: 'Maneuver',
  mass: 20722.14,
  cost: 10667.73,
  tl: 'I',
};

export const sampleWithPartialData: IEngines = {
  id: 23681,
  eShipId: 3785,
  engine: 'Power',
  mass: 4520.78,
  cost: 25255.91,
  tl: 'B',
};

export const sampleWithFullData: IEngines = {
  id: 17602,
  eShipId: 1161,
  engine: 'Jump',
  mass: 16865.87,
  cost: 11820.52,
  tl: 'E',
};

export const sampleWithNewData: NewEngines = {
  eShipId: 11954,
  engine: 'Jump',
  mass: 24381.78,
  cost: 28427.55,
  tl: 'L',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
