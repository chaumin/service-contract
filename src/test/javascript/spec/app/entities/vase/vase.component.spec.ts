/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ServiceContractTestModule } from '../../../test.module';
import { VaseComponent } from 'app/entities/vase/vase.component';
import { VaseService } from 'app/entities/vase/vase.service';
import { Vase } from 'app/shared/model/vase.model';

describe('Component Tests', () => {
    describe('Vase Management Component', () => {
        let comp: VaseComponent;
        let fixture: ComponentFixture<VaseComponent>;
        let service: VaseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ServiceContractTestModule],
                declarations: [VaseComponent],
                providers: []
            })
                .overrideTemplate(VaseComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VaseComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VaseService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Vase(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.vases[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
