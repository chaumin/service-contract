import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ServiceContractSharedModule } from 'app/shared';
import {
    ServiceContractComponent,
    serviceContractRoute
} from './';

const ENTITY_STATES = [...serviceContractRoute];

@NgModule({
    imports: [ServiceContractSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ServiceContractComponent
    ],
    entryComponents: [ServiceContractComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServiceContractModule {}
