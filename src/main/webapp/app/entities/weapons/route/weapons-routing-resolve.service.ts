import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWeapons } from '../weapons.model';
import { WeaponsService } from '../service/weapons.service';

const weaponsResolve = (route: ActivatedRouteSnapshot): Observable<null | IWeapons> => {
  const id = route.params.id;
  if (id) {
    return inject(WeaponsService)
      .find(id)
      .pipe(
        mergeMap((weapons: HttpResponse<IWeapons>) => {
          if (weapons.body) {
            return of(weapons.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default weaponsResolve;
