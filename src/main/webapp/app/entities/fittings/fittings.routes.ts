import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FittingsResolve from './route/fittings-routing-resolve.service';

const fittingsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fittings.component').then(m => m.FittingsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fittings-detail.component').then(m => m.FittingsDetailComponent),
    resolve: {
      fittings: FittingsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fittings-update.component').then(m => m.FittingsUpdateComponent),
    resolve: {
      fittings: FittingsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fittings-update.component').then(m => m.FittingsUpdateComponent),
    resolve: {
      fittings: FittingsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fittingsRoute;
