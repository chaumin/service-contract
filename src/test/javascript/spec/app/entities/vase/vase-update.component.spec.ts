/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ServiceContractTestModule } from '../../../test.module';
import { VaseUpdateComponent } from 'app/entities/vase/vase-update.component';
import { VaseService } from 'app/entities/vase/vase.service';
import { Vase } from 'app/shared/model/vase.model';

describe('Component Tests', () => {
    describe('Vase Management Update Component', () => {
        let comp: VaseUpdateComponent;
        let fixture: ComponentFixture<VaseUpdateComponent>;
        let service: VaseService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ServiceContractTestModule],
                declarations: [VaseUpdateComponent]
            })
                .overrideTemplate(VaseUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VaseUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VaseService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Vase(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.vase = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Vase();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.vase = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
