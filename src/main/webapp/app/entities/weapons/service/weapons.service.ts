import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWeapons, NewWeapons } from '../weapons.model';

export type PartialUpdateWeapons = Partial<IWeapons> & Pick<IWeapons, 'id'>;

export type EntityResponseType = HttpResponse<IWeapons>;
export type EntityArrayResponseType = HttpResponse<IWeapons[]>;

@Injectable({ providedIn: 'root' })
export class WeaponsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/weapons');

  create(weapons: NewWeapons): Observable<EntityResponseType> {
    return this.http.post<IWeapons>(this.resourceUrl, weapons, { observe: 'response' });
  }

  update(weapons: IWeapons): Observable<EntityResponseType> {
    return this.http.put<IWeapons>(`${this.resourceUrl}/${this.getWeaponsIdentifier(weapons)}`, weapons, { observe: 'response' });
  }

  partialUpdate(weapons: PartialUpdateWeapons): Observable<EntityResponseType> {
    return this.http.patch<IWeapons>(`${this.resourceUrl}/${this.getWeaponsIdentifier(weapons)}`, weapons, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWeapons>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWeapons[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWeaponsIdentifier(weapons: Pick<IWeapons, 'id'>): number {
    return weapons.id;
  }

  compareWeapons(o1: Pick<IWeapons, 'id'> | null, o2: Pick<IWeapons, 'id'> | null): boolean {
    return o1 && o2 ? this.getWeaponsIdentifier(o1) === this.getWeaponsIdentifier(o2) : o1 === o2;
  }

  addWeaponsToCollectionIfMissing<Type extends Pick<IWeapons, 'id'>>(
    weaponsCollection: Type[],
    ...weaponsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const weapons: Type[] = weaponsToCheck.filter(isPresent);
    if (weapons.length > 0) {
      const weaponsCollectionIdentifiers = weaponsCollection.map(weaponsItem => this.getWeaponsIdentifier(weaponsItem));
      const weaponsToAdd = weapons.filter(weaponsItem => {
        const weaponsIdentifier = this.getWeaponsIdentifier(weaponsItem);
        if (weaponsCollectionIdentifiers.includes(weaponsIdentifier)) {
          return false;
        }
        weaponsCollectionIdentifiers.push(weaponsIdentifier);
        return true;
      });
      return [...weaponsToAdd, ...weaponsCollection];
    }
    return weaponsCollection;
  }
}
