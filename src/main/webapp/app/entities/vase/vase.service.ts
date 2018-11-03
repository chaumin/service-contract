import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVase } from 'app/shared/model/vase.model';

type EntityResponseType = HttpResponse<IVase>;
type EntityArrayResponseType = HttpResponse<IVase[]>;

@Injectable({ providedIn: 'root' })
export class VaseService {
    private resourceUrl = SERVER_API_URL + 'api/vases';

    constructor(private http: HttpClient) {}

    create(vase: IVase): Observable<EntityResponseType> {
        return this.http.post<IVase>(this.resourceUrl, vase, { observe: 'response' });
    }

    update(vase: IVase): Observable<EntityResponseType> {
        return this.http.put<IVase>(this.resourceUrl, vase, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IVase>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IVase[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
