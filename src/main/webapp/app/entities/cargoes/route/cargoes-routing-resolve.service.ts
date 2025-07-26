import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICargoes } from '../cargoes.model';
import { CargoesService } from '../service/cargoes.service';

const cargoesResolve = (route: ActivatedRouteSnapshot): Observable<null | ICargoes> => {
  const id = route.params.id;
  if (id) {
    return inject(CargoesService)
      .find(id)
      .pipe(
        mergeMap((cargoes: HttpResponse<ICargoes>) => {
          if (cargoes.body) {
            return of(cargoes.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cargoesResolve;
