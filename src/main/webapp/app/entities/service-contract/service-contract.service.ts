import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IContract } from 'app/shared/model/contract.model';

type EntityResponseType = HttpResponse<IContract>;
type EntityArrayResponseType = HttpResponse<IContract[]>;

@Injectable({ providedIn: 'root' })
export class ServiceContractService {
    private resourceUrl = SERVER_API_URL + 'api/service-contract';

    constructor(private http: HttpClient) {}

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IContract[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

}
