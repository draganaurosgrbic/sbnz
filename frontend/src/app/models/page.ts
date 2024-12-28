export interface Page<T>{
    content: T[];
    first: boolean;
    last: boolean;
    pageable: {
        pageNumber: number;
    };
}
