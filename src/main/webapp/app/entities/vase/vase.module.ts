import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServiceContractSharedModule } from 'app/shared';
import {
    VaseComponent,
    VaseDetailComponent,
    VaseUpdateComponent,
    VaseDeletePopupComponent,
    VaseDeleteDialogComponent,
    vaseRoute,
    vasePopupRoute
} from './';

const ENTITY_STATES = [...vaseRoute, ...vasePopupRoute];

@NgModule({
    imports: [ServiceContractSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [VaseComponent, VaseDetailComponent, VaseUpdateComponent, VaseDeleteDialogComponent, VaseDeletePopupComponent],
    entryComponents: [VaseComponent, VaseUpdateComponent, VaseDeleteDialogComponent, VaseDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServiceContractVaseModule {}
