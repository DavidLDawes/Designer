import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFittings } from '../fittings.model';
import { FittingsService } from '../service/fittings.service';

const fittingsResolve = (route: ActivatedRouteSnapshot): Observable<null | IFittings> => {
  const id = route.params.id;
  if (id) {
    return inject(FittingsService)
      .find(id)
      .pipe(
        mergeMap((fittings: HttpResponse<IFittings>) => {
          if (fittings.body) {
            return of(fittings.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default fittingsResolve;
