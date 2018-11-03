import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Contract } from 'app/shared/model/contract.model';
import { ServiceContractService } from './service-contract.service';
import { ServiceContractComponent } from './service-contract.component';
import { IContract } from 'app/shared/model/contract.model';

@Injectable({ providedIn: 'root' })
export class ServiceContractResolve implements Resolve<IContract> {
    constructor(private service: ServiceContractService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        // if (id) {
        //     return this.service.find(id).pipe(map((contract: HttpResponse<Contract>) => contract.body));
        // }
        return of(new Contract());
    }
}

export const serviceContractRoute: Routes = [
    {
        path: 'service-contract',
        component: ServiceContractComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Service Contract'
        },
        canActivate: [UserRouteAccessService]
    }
];
