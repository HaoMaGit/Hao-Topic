declare type Merge<T, P> = {
    [K in keyof T & keyof P]: P[K] | T[K];
};
export declare function mergeFuncProps<T extends Record<string, any>, P extends Record<string, any>>(p1: T, p2: P): Merge<T, P>;
export {};
