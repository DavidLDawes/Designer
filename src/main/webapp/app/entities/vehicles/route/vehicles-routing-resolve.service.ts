import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicles } from '../vehicles.model';
import { VehiclesService } from '../service/vehicles.service';

const vehiclesResolve = (route: ActivatedRouteSnapshot): Observable<null | IVehicles> => {
  const id = route.params.id;
  if (id) {
    return inject(VehiclesService)
      .find(id)
      .pipe(
        mergeMap((vehicles: HttpResponse<IVehicles>) => {
          if (vehicles.body) {
            return of(vehicles.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vehiclesResolve;
