import { IShips, NewShips } from './ships.model';

export const sampleWithRequiredData: IShips = {
  id: 32733,
  shipId: 18278,
  userId: 9512,
  tl: 'G',
  tonnage: 6128,
  conf: 'Planetoid',
  code: 'maintainer questionably subdued',
  hull: 23184,
  structure: 2,
  sections: 14172,
  capital: true,
  military: true,
};

export const sampleWithPartialData: IShips = {
  id: 9978,
  shipId: 22606,
  userId: 5317,
  tl: 'L',
  tonnage: 18806,
  conf: 'Structure',
  code: 'acceptable',
  hull: 31433,
  structure: 32140,
  sections: 26810,
  capital: true,
  military: false,
};

export const sampleWithFullData: IShips = {
  id: 27629,
  shipId: 7467,
  userId: 18991,
  tl: 'D',
  tonnage: 10788,
  conf: 'Wedge',
  code: 'censor',
  hull: 19018,
  structure: 13292,
  sections: 24356,
  capital: true,
  military: true,
};

export const sampleWithNewData: NewShips = {
  shipId: 17754,
  userId: 19060,
  tl: 'C',
  tonnage: 1115,
  conf: 'Dispersed',
  code: 'aha although',
  hull: 10736,
  structure: 2942,
  sections: 12051,
  capital: false,
  military: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
