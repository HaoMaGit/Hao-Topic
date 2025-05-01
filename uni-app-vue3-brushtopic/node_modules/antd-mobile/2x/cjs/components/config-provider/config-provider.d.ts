import { FC, ReactNode } from 'react';
import { Locale } from '../../locales/base';
declare type Config = {
    locale: Locale;
    checkList?: {
        activeIcon?: ReactNode;
    };
    collapse?: {
        arrowIcon?: ReactNode | ((active: boolean) => ReactNode);
    };
    dropdown?: {
        arrowIcon?: ReactNode;
    };
    form?: {
        helpIcon?: ReactNode;
    };
    input?: {
        clearIcon?: ReactNode;
    };
    list?: {
        arrowIcon?: ReactNode;
    };
    navBar?: {
        backIcon?: ReactNode;
    };
    noticeBar?: {
        icon?: ReactNode;
        closeIcon?: ReactNode;
    };
    popup?: {
        closeIcon?: ReactNode;
    };
    result?: {
        successIcon?: ReactNode;
        errorIcon?: ReactNode;
        infoIcon?: ReactNode;
        waitingIcon?: ReactNode;
        warningIcon?: ReactNode;
    };
    searchBar?: {
        searchIcon?: ReactNode;
    };
};
export declare const defaultConfigRef: {
    current: Config;
};
export declare function setDefaultConfig(config: Config): void;
export declare function getDefaultConfig(): Config;
export declare type ConfigProviderProps = Partial<Config> & {
    children?: ReactNode;
};
export declare const ConfigProvider: FC<ConfigProviderProps>;
export declare function useConfig(): Config;
export {};
