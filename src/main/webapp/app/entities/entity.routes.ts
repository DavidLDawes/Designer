import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'designerApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'ships',
    data: { pageTitle: 'designerApp.ships.home.title' },
    loadChildren: () => import('./ships/ships.routes'),
  },
  {
    path: 'fittings',
    data: { pageTitle: 'designerApp.fittings.home.title' },
    loadChildren: () => import('./fittings/fittings.routes'),
  },
  {
    path: 'weapons',
    data: { pageTitle: 'designerApp.weapons.home.title' },
    loadChildren: () => import('./weapons/weapons.routes'),
  },
  {
    path: 'defenses',
    data: { pageTitle: 'designerApp.defenses.home.title' },
    loadChildren: () => import('./defenses/defenses.routes'),
  },
  {
    path: 'cargoes',
    data: { pageTitle: 'designerApp.cargoes.home.title' },
    loadChildren: () => import('./cargoes/cargoes.routes'),
  },
  {
    path: 'vehicles',
    data: { pageTitle: 'designerApp.vehicles.home.title' },
    loadChildren: () => import('./vehicles/vehicles.routes'),
  },
  {
    path: 'engines',
    data: { pageTitle: 'designerApp.engines.home.title' },
    loadChildren: () => import('./engines/engines.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
