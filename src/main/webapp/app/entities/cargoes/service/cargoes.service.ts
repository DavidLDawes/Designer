import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICargoes, NewCargoes } from '../cargoes.model';

export type PartialUpdateCargoes = Partial<ICargoes> & Pick<ICargoes, 'id'>;

export type EntityResponseType = HttpResponse<ICargoes>;
export type EntityArrayResponseType = HttpResponse<ICargoes[]>;

@Injectable({ providedIn: 'root' })
export class CargoesService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cargoes');

  create(cargoes: NewCargoes): Observable<EntityResponseType> {
    return this.http.post<ICargoes>(this.resourceUrl, cargoes, { observe: 'response' });
  }

  update(cargoes: ICargoes): Observable<EntityResponseType> {
    return this.http.put<ICargoes>(`${this.resourceUrl}/${this.getCargoesIdentifier(cargoes)}`, cargoes, { observe: 'response' });
  }

  partialUpdate(cargoes: PartialUpdateCargoes): Observable<EntityResponseType> {
    return this.http.patch<ICargoes>(`${this.resourceUrl}/${this.getCargoesIdentifier(cargoes)}`, cargoes, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICargoes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICargoes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCargoesIdentifier(cargoes: Pick<ICargoes, 'id'>): number {
    return cargoes.id;
  }

  compareCargoes(o1: Pick<ICargoes, 'id'> | null, o2: Pick<ICargoes, 'id'> | null): boolean {
    return o1 && o2 ? this.getCargoesIdentifier(o1) === this.getCargoesIdentifier(o2) : o1 === o2;
  }

  addCargoesToCollectionIfMissing<Type extends Pick<ICargoes, 'id'>>(
    cargoesCollection: Type[],
    ...cargoesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cargoes: Type[] = cargoesToCheck.filter(isPresent);
    if (cargoes.length > 0) {
      const cargoesCollectionIdentifiers = cargoesCollection.map(cargoesItem => this.getCargoesIdentifier(cargoesItem));
      const cargoesToAdd = cargoes.filter(cargoesItem => {
        const cargoesIdentifier = this.getCargoesIdentifier(cargoesItem);
        if (cargoesCollectionIdentifiers.includes(cargoesIdentifier)) {
          return false;
        }
        cargoesCollectionIdentifiers.push(cargoesIdentifier);
        return true;
      });
      return [...cargoesToAdd, ...cargoesCollection];
    }
    return cargoesCollection;
  }
}
