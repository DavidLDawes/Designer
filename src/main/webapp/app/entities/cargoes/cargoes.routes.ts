import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CargoesResolve from './route/cargoes-routing-resolve.service';

const cargoesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cargoes.component').then(m => m.CargoesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cargoes-detail.component').then(m => m.CargoesDetailComponent),
    resolve: {
      cargoes: CargoesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cargoes-update.component').then(m => m.CargoesUpdateComponent),
    resolve: {
      cargoes: CargoesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cargoes-update.component').then(m => m.CargoesUpdateComponent),
    resolve: {
      cargoes: CargoesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cargoesRoute;
