import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EnginesResolve from './route/engines-routing-resolve.service';

const enginesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/engines.component').then(m => m.EnginesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/engines-detail.component').then(m => m.EnginesDetailComponent),
    resolve: {
      engines: EnginesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/engines-update.component').then(m => m.EnginesUpdateComponent),
    resolve: {
      engines: EnginesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/engines-update.component').then(m => m.EnginesUpdateComponent),
    resolve: {
      engines: EnginesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default enginesRoute;
