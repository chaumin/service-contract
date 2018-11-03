import { IVase } from 'app/shared/model//vase.model';

export interface IProduct {
    id?: number;
    productType?: string;
    commitment?: number;
    price?: number;
    name?: string;
    device?: string;
    serviceId?: string;
    vases?: IVase[];
    contractId?: number;
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public productType?: string,
        public commitment?: number,
        public price?: number,
        public name?: string,
        public device?: string,
        public serviceId?: string,
        public vases?: IVase[],
        public contractId?: number
    ) {}
}
