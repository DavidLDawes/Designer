import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ShipsResolve from './route/ships-routing-resolve.service';

const shipsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ships.component').then(m => m.ShipsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ships-detail.component').then(m => m.ShipsDetailComponent),
    resolve: {
      ships: ShipsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ships-update.component').then(m => m.ShipsUpdateComponent),
    resolve: {
      ships: ShipsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ships-update.component').then(m => m.ShipsUpdateComponent),
    resolve: {
      ships: ShipsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default shipsRoute;
