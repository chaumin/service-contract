import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IVase } from 'app/shared/model/vase.model';
import { VaseService } from './vase.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';

@Component({
    selector: 'jhi-vase-update',
    templateUrl: './vase-update.component.html'
})
export class VaseUpdateComponent implements OnInit {
    private _vase: IVase;
    isSaving: boolean;

    products: IProduct[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private vaseService: VaseService,
        private productService: ProductService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ vase }) => {
            this.vase = vase;
        });
        this.productService.query().subscribe(
            (res: HttpResponse<IProduct[]>) => {
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.vase.id !== undefined) {
            this.subscribeToSaveResponse(this.vaseService.update(this.vase));
        } else {
            this.subscribeToSaveResponse(this.vaseService.create(this.vase));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IVase>>) {
        result.subscribe((res: HttpResponse<IVase>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProductById(index: number, item: IProduct) {
        return item.id;
    }
    get vase() {
        return this._vase;
    }

    set vase(vase: IVase) {
        this._vase = vase;
    }
}
