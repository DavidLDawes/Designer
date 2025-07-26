import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEngines, NewEngines } from '../engines.model';

export type PartialUpdateEngines = Partial<IEngines> & Pick<IEngines, 'id'>;

export type EntityResponseType = HttpResponse<IEngines>;
export type EntityArrayResponseType = HttpResponse<IEngines[]>;

@Injectable({ providedIn: 'root' })
export class EnginesService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/engines');

  create(engines: NewEngines): Observable<EntityResponseType> {
    return this.http.post<IEngines>(this.resourceUrl, engines, { observe: 'response' });
  }

  update(engines: IEngines): Observable<EntityResponseType> {
    return this.http.put<IEngines>(`${this.resourceUrl}/${this.getEnginesIdentifier(engines)}`, engines, { observe: 'response' });
  }

  partialUpdate(engines: PartialUpdateEngines): Observable<EntityResponseType> {
    return this.http.patch<IEngines>(`${this.resourceUrl}/${this.getEnginesIdentifier(engines)}`, engines, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEngines>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEngines[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEnginesIdentifier(engines: Pick<IEngines, 'id'>): number {
    return engines.id;
  }

  compareEngines(o1: Pick<IEngines, 'id'> | null, o2: Pick<IEngines, 'id'> | null): boolean {
    return o1 && o2 ? this.getEnginesIdentifier(o1) === this.getEnginesIdentifier(o2) : o1 === o2;
  }

  addEnginesToCollectionIfMissing<Type extends Pick<IEngines, 'id'>>(
    enginesCollection: Type[],
    ...enginesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const engines: Type[] = enginesToCheck.filter(isPresent);
    if (engines.length > 0) {
      const enginesCollectionIdentifiers = enginesCollection.map(enginesItem => this.getEnginesIdentifier(enginesItem));
      const enginesToAdd = engines.filter(enginesItem => {
        const enginesIdentifier = this.getEnginesIdentifier(enginesItem);
        if (enginesCollectionIdentifiers.includes(enginesIdentifier)) {
          return false;
        }
        enginesCollectionIdentifiers.push(enginesIdentifier);
        return true;
      });
      return [...enginesToAdd, ...enginesCollection];
    }
    return enginesCollection;
  }
}
