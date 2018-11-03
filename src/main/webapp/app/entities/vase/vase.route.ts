import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Vase } from 'app/shared/model/vase.model';
import { VaseService } from './vase.service';
import { VaseComponent } from './vase.component';
import { VaseDetailComponent } from './vase-detail.component';
import { VaseUpdateComponent } from './vase-update.component';
import { VaseDeletePopupComponent } from './vase-delete-dialog.component';
import { IVase } from 'app/shared/model/vase.model';

@Injectable({ providedIn: 'root' })
export class VaseResolve implements Resolve<IVase> {
    constructor(private service: VaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((vase: HttpResponse<Vase>) => vase.body));
        }
        return of(new Vase());
    }
}

export const vaseRoute: Routes = [
    {
        path: 'vase',
        component: VaseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Vases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vase/:id/view',
        component: VaseDetailComponent,
        resolve: {
            vase: VaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Vases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vase/new',
        component: VaseUpdateComponent,
        resolve: {
            vase: VaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Vases'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vase/:id/edit',
        component: VaseUpdateComponent,
        resolve: {
            vase: VaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Vases'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vasePopupRoute: Routes = [
    {
        path: 'vase/:id/delete',
        component: VaseDeletePopupComponent,
        resolve: {
            vase: VaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Vases'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
