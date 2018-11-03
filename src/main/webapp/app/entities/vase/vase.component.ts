import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IVase } from 'app/shared/model/vase.model';
import { Principal } from 'app/core';
import { VaseService } from './vase.service';

@Component({
    selector: 'jhi-vase',
    templateUrl: './vase.component.html'
})
export class VaseComponent implements OnInit, OnDestroy {
    vases: IVase[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private vaseService: VaseService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.vaseService.query().subscribe(
            (res: HttpResponse<IVase[]>) => {
                this.vases = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInVases();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IVase) {
        return item.id;
    }

    registerChangeInVases() {
        this.eventSubscriber = this.eventManager.subscribe('vaseListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
