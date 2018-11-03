import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ServiceContractContractModule } from './contract/contract.module';
import { ServiceContractProductModule } from './product/product.module';
import { ServiceContractVaseModule } from './vase/vase.module';
import { ServiceContractModule } from './service-contract/service-contract.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ServiceContractContractModule,
        ServiceContractProductModule,
        ServiceContractVaseModule,
        ServiceContractModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServiceContractEntityModule {}
