import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFittings, NewFittings } from '../fittings.model';

export type PartialUpdateFittings = Partial<IFittings> & Pick<IFittings, 'id'>;

export type EntityResponseType = HttpResponse<IFittings>;
export type EntityArrayResponseType = HttpResponse<IFittings[]>;

@Injectable({ providedIn: 'root' })
export class FittingsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fittings');

  create(fittings: NewFittings): Observable<EntityResponseType> {
    return this.http.post<IFittings>(this.resourceUrl, fittings, { observe: 'response' });
  }

  update(fittings: IFittings): Observable<EntityResponseType> {
    return this.http.put<IFittings>(`${this.resourceUrl}/${this.getFittingsIdentifier(fittings)}`, fittings, { observe: 'response' });
  }

  partialUpdate(fittings: PartialUpdateFittings): Observable<EntityResponseType> {
    return this.http.patch<IFittings>(`${this.resourceUrl}/${this.getFittingsIdentifier(fittings)}`, fittings, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFittings>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFittings[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFittingsIdentifier(fittings: Pick<IFittings, 'id'>): number {
    return fittings.id;
  }

  compareFittings(o1: Pick<IFittings, 'id'> | null, o2: Pick<IFittings, 'id'> | null): boolean {
    return o1 && o2 ? this.getFittingsIdentifier(o1) === this.getFittingsIdentifier(o2) : o1 === o2;
  }

  addFittingsToCollectionIfMissing<Type extends Pick<IFittings, 'id'>>(
    fittingsCollection: Type[],
    ...fittingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fittings: Type[] = fittingsToCheck.filter(isPresent);
    if (fittings.length > 0) {
      const fittingsCollectionIdentifiers = fittingsCollection.map(fittingsItem => this.getFittingsIdentifier(fittingsItem));
      const fittingsToAdd = fittings.filter(fittingsItem => {
        const fittingsIdentifier = this.getFittingsIdentifier(fittingsItem);
        if (fittingsCollectionIdentifiers.includes(fittingsIdentifier)) {
          return false;
        }
        fittingsCollectionIdentifiers.push(fittingsIdentifier);
        return true;
      });
      return [...fittingsToAdd, ...fittingsCollection];
    }
    return fittingsCollection;
  }
}
