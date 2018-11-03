import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ServiceContractModule } from './service-contract/service-contract.module';

@NgModule({
    imports: [
        ServiceContractModule
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServiceContractEntityModule {}
