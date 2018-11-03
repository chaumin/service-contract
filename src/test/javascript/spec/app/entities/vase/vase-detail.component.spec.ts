/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServiceContractTestModule } from '../../../test.module';
import { VaseDetailComponent } from 'app/entities/vase/vase-detail.component';
import { Vase } from 'app/shared/model/vase.model';

describe('Component Tests', () => {
    describe('Vase Management Detail Component', () => {
        let comp: VaseDetailComponent;
        let fixture: ComponentFixture<VaseDetailComponent>;
        const route = ({ data: of({ vase: new Vase(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ServiceContractTestModule],
                declarations: [VaseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(VaseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VaseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.vase).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
