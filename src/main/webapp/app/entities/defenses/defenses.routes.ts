import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DefensesResolve from './route/defenses-routing-resolve.service';

const defensesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/defenses.component').then(m => m.DefensesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/defenses-detail.component').then(m => m.DefensesDetailComponent),
    resolve: {
      defenses: DefensesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/defenses-update.component').then(m => m.DefensesUpdateComponent),
    resolve: {
      defenses: DefensesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/defenses-update.component').then(m => m.DefensesUpdateComponent),
    resolve: {
      defenses: DefensesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default defensesRoute;
