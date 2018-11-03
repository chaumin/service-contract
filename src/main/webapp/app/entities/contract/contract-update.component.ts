import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IContract } from 'app/shared/model/contract.model';
import { ContractService } from './contract.service';

@Component({
    selector: 'jhi-contract-update',
    templateUrl: './contract-update.component.html'
})
export class ContractUpdateComponent implements OnInit {
    private _contract: IContract;
    isSaving: boolean;

    constructor(private dataUtils: JhiDataUtils, private contractService: ContractService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
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

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.contract.id !== undefined) {
            this.subscribeToSaveResponse(this.contractService.update(this.contract));
        } else {
            this.subscribeToSaveResponse(this.contractService.create(this.contract));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>) {
        result.subscribe((res: HttpResponse<IContract>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get contract() {
        return this._contract;
    }

    set contract(contract: IContract) {
        this._contract = contract;
    }
}
