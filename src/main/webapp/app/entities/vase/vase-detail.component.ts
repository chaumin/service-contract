import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVase } from 'app/shared/model/vase.model';

@Component({
    selector: 'jhi-vase-detail',
    templateUrl: './vase-detail.component.html'
})
export class VaseDetailComponent implements OnInit {
    vase: IVase;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ vase }) => {
            this.vase = vase;
        });
    }

    previousState() {
        window.history.back();
    }
}
