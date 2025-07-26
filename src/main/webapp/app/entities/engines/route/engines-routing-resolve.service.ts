import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEngines } from '../engines.model';
import { EnginesService } from '../service/engines.service';

const enginesResolve = (route: ActivatedRouteSnapshot): Observable<null | IEngines> => {
  const id = route.params.id;
  if (id) {
    return inject(EnginesService)
      .find(id)
      .pipe(
        mergeMap((engines: HttpResponse<IEngines>) => {
          if (engines.body) {
            return of(engines.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default enginesResolve;
