import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShips } from '../ships.model';
import { ShipsService } from '../service/ships.service';

const shipsResolve = (route: ActivatedRouteSnapshot): Observable<null | IShips> => {
  const id = route.params.id;
  if (id) {
    return inject(ShipsService)
      .find(id)
      .pipe(
        mergeMap((ships: HttpResponse<IShips>) => {
          if (ships.body) {
            return of(ships.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default shipsResolve;
