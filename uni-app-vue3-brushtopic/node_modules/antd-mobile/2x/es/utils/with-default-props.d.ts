export declare function mergeProps<A, B>(a: A, b: B): B & A;
export declare function mergeProps<A, B, C>(a: A, b: B, c: C): C & B & A;
export declare function mergeProps<A, B, C, D>(a: A, b: B, c: C, d: D): D & C & B & A;
/**
 * Merge props and return the first non-undefined value.
 * The later has higher priority. e.g. (10, 1, 5) => 5 wins.
 * This is useful with legacy props that have been deprecated.
 */
export declare function mergeProp<T, DefaultT extends T = T>(defaultProp: DefaultT, ...propList: T[]): T | undefined;
