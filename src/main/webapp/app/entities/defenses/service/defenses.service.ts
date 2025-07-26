import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDefenses, NewDefenses } from '../defenses.model';

export type PartialUpdateDefenses = Partial<IDefenses> & Pick<IDefenses, 'id'>;

export type EntityResponseType = HttpResponse<IDefenses>;
export type EntityArrayResponseType = HttpResponse<IDefenses[]>;

@Injectable({ providedIn: 'root' })
export class DefensesService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/defenses');

  create(defenses: NewDefenses): Observable<EntityResponseType> {
    return this.http.post<IDefenses>(this.resourceUrl, defenses, { observe: 'response' });
  }

  update(defenses: IDefenses): Observable<EntityResponseType> {
    return this.http.put<IDefenses>(`${this.resourceUrl}/${this.getDefensesIdentifier(defenses)}`, defenses, { observe: 'response' });
  }

  partialUpdate(defenses: PartialUpdateDefenses): Observable<EntityResponseType> {
    return this.http.patch<IDefenses>(`${this.resourceUrl}/${this.getDefensesIdentifier(defenses)}`, defenses, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDefenses>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDefenses[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDefensesIdentifier(defenses: Pick<IDefenses, 'id'>): number {
    return defenses.id;
  }

  compareDefenses(o1: Pick<IDefenses, 'id'> | null, o2: Pick<IDefenses, 'id'> | null): boolean {
    return o1 && o2 ? this.getDefensesIdentifier(o1) === this.getDefensesIdentifier(o2) : o1 === o2;
  }

  addDefensesToCollectionIfMissing<Type extends Pick<IDefenses, 'id'>>(
    defensesCollection: Type[],
    ...defensesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const defenses: Type[] = defensesToCheck.filter(isPresent);
    if (defenses.length > 0) {
      const defensesCollectionIdentifiers = defensesCollection.map(defensesItem => this.getDefensesIdentifier(defensesItem));
      const defensesToAdd = defenses.filter(defensesItem => {
        const defensesIdentifier = this.getDefensesIdentifier(defensesItem);
        if (defensesCollectionIdentifiers.includes(defensesIdentifier)) {
          return false;
        }
        defensesCollectionIdentifiers.push(defensesIdentifier);
        return true;
      });
      return [...defensesToAdd, ...defensesCollection];
    }
    return defensesCollection;
  }
}
