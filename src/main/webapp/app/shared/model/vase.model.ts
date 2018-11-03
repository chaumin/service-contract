export interface IVase {
    id?: number;
    name?: string;
    productId?: number;
}

export class Vase implements IVase {
    constructor(public id?: number, public name?: string, public productId?: number) {}
}
