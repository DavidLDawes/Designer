import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VehiclesResolve from './route/vehicles-routing-resolve.service';

const vehiclesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vehicles.component').then(m => m.VehiclesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vehicles-detail.component').then(m => m.VehiclesDetailComponent),
    resolve: {
      vehicles: VehiclesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vehicles-update.component').then(m => m.VehiclesUpdateComponent),
    resolve: {
      vehicles: VehiclesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vehicles-update.component').then(m => m.VehiclesUpdateComponent),
    resolve: {
      vehicles: VehiclesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehiclesRoute;
