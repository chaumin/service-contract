import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IContract } from 'app/shared/model/contract.model';

@Component({
    selector: 'jhi-contract-detail',
    templateUrl: './contract-detail.component.html'
})
export class ContractDetailComponent implements OnInit {
    contract: IContract;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ contract }) => {
            this.contract = contract;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
