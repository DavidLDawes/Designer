import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShips, NewShips } from '../ships.model';

export type PartialUpdateShips = Partial<IShips> & Pick<IShips, 'id'>;

export type EntityResponseType = HttpResponse<IShips>;
export type EntityArrayResponseType = HttpResponse<IShips[]>;

@Injectable({ providedIn: 'root' })
export class ShipsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ships');

  create(ships: NewShips): Observable<EntityResponseType> {
    return this.http.post<IShips>(this.resourceUrl, ships, { observe: 'response' });
  }

  update(ships: IShips): Observable<EntityResponseType> {
    return this.http.put<IShips>(`${this.resourceUrl}/${this.getShipsIdentifier(ships)}`, ships, { observe: 'response' });
  }

  partialUpdate(ships: PartialUpdateShips): Observable<EntityResponseType> {
    return this.http.patch<IShips>(`${this.resourceUrl}/${this.getShipsIdentifier(ships)}`, ships, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShips>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShips[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getShipsIdentifier(ships: Pick<IShips, 'id'>): number {
    return ships.id;
  }

  compareShips(o1: Pick<IShips, 'id'> | null, o2: Pick<IShips, 'id'> | null): boolean {
    return o1 && o2 ? this.getShipsIdentifier(o1) === this.getShipsIdentifier(o2) : o1 === o2;
  }

  addShipsToCollectionIfMissing<Type extends Pick<IShips, 'id'>>(
    shipsCollection: Type[],
    ...shipsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ships: Type[] = shipsToCheck.filter(isPresent);
    if (ships.length > 0) {
      const shipsCollectionIdentifiers = shipsCollection.map(shipsItem => this.getShipsIdentifier(shipsItem));
      const shipsToAdd = ships.filter(shipsItem => {
        const shipsIdentifier = this.getShipsIdentifier(shipsItem);
        if (shipsCollectionIdentifiers.includes(shipsIdentifier)) {
          return false;
        }
        shipsCollectionIdentifiers.push(shipsIdentifier);
        return true;
      });
      return [...shipsToAdd, ...shipsCollection];
    }
    return shipsCollection;
  }
}
