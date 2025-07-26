import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDefenses } from '../defenses.model';
import { DefensesService } from '../service/defenses.service';

const defensesResolve = (route: ActivatedRouteSnapshot): Observable<null | IDefenses> => {
  const id = route.params.id;
  if (id) {
    return inject(DefensesService)
      .find(id)
      .pipe(
        mergeMap((defenses: HttpResponse<IDefenses>) => {
          if (defenses.body) {
            return of(defenses.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default defensesResolve;
