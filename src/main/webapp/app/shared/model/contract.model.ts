import { IProduct } from 'app/shared/model/product.model';

export interface IContract {
    id?: number;
    customerName?: string;
    customerAge?: number;
    customerDocId?: string;
    contentContentType?: string;
    content?: any;
    products?: IProduct[];
    createdDate?: Date;
}

export class Contract implements IContract {
    constructor(
        public id?: number,
        public customerName?: string,
        public customerAge?: number,
        public customerDocId?: string,
        public contentContentType?: string,
        public content?: any,
        public products?: IProduct[],
        public createdDate?: Date
    ) {}
}
