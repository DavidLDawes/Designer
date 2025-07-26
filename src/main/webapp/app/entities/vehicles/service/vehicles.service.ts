import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicles, NewVehicles } from '../vehicles.model';

export type PartialUpdateVehicles = Partial<IVehicles> & Pick<IVehicles, 'id'>;

export type EntityResponseType = HttpResponse<IVehicles>;
export type EntityArrayResponseType = HttpResponse<IVehicles[]>;

@Injectable({ providedIn: 'root' })
export class VehiclesService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicles');

  create(vehicles: NewVehicles): Observable<EntityResponseType> {
    return this.http.post<IVehicles>(this.resourceUrl, vehicles, { observe: 'response' });
  }

  update(vehicles: IVehicles): Observable<EntityResponseType> {
    return this.http.put<IVehicles>(`${this.resourceUrl}/${this.getVehiclesIdentifier(vehicles)}`, vehicles, { observe: 'response' });
  }

  partialUpdate(vehicles: PartialUpdateVehicles): Observable<EntityResponseType> {
    return this.http.patch<IVehicles>(`${this.resourceUrl}/${this.getVehiclesIdentifier(vehicles)}`, vehicles, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicles>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicles[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehiclesIdentifier(vehicles: Pick<IVehicles, 'id'>): number {
    return vehicles.id;
  }

  compareVehicles(o1: Pick<IVehicles, 'id'> | null, o2: Pick<IVehicles, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehiclesIdentifier(o1) === this.getVehiclesIdentifier(o2) : o1 === o2;
  }

  addVehiclesToCollectionIfMissing<Type extends Pick<IVehicles, 'id'>>(
    vehiclesCollection: Type[],
    ...vehiclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicles: Type[] = vehiclesToCheck.filter(isPresent);
    if (vehicles.length > 0) {
      const vehiclesCollectionIdentifiers = vehiclesCollection.map(vehiclesItem => this.getVehiclesIdentifier(vehiclesItem));
      const vehiclesToAdd = vehicles.filter(vehiclesItem => {
        const vehiclesIdentifier = this.getVehiclesIdentifier(vehiclesItem);
        if (vehiclesCollectionIdentifiers.includes(vehiclesIdentifier)) {
          return false;
        }
        vehiclesCollectionIdentifiers.push(vehiclesIdentifier);
        return true;
      });
      return [...vehiclesToAdd, ...vehiclesCollection];
    }
    return vehiclesCollection;
  }
}
