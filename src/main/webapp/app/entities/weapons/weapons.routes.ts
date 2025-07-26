import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WeaponsResolve from './route/weapons-routing-resolve.service';

const weaponsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/weapons.component').then(m => m.WeaponsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/weapons-detail.component').then(m => m.WeaponsDetailComponent),
    resolve: {
      weapons: WeaponsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/weapons-update.component').then(m => m.WeaponsUpdateComponent),
    resolve: {
      weapons: WeaponsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/weapons-update.component').then(m => m.WeaponsUpdateComponent),
    resolve: {
      weapons: WeaponsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default weaponsRoute;
