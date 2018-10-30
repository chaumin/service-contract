import { NgModule } from '@angular/core';

import { ServiceContractSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ServiceContractSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ServiceContractSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ServiceContractSharedCommonModule {}
