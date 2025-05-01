import type { CSSProperties, InputHTMLAttributes, ReactElement, ReactNode } from 'react';
import React from 'react';
import { NativeProps } from '../../utils/native-props';
import { GridProps } from '../grid';
import type { ImageProps } from '../image';
export declare type TaskStatus = 'pending' | 'fail' | 'success';
export interface ImageUploadItem {
    key?: string | number;
    url: string;
    thumbnailUrl?: string;
    extra?: any;
}
declare type Task = {
    id: number;
    url?: string;
    file: File;
    status: TaskStatus;
};
export declare type UploadTask = Pick<Task, 'id' | 'status'>;
export declare type ImageUploaderProps = {
    defaultValue?: ImageUploadItem[];
    value?: ImageUploadItem[];
    columns?: GridProps['columns'];
    onChange?: (items: ImageUploadItem[]) => void;
    onUploadQueueChange?: (tasks: UploadTask[]) => void;
    accept?: string;
    multiple?: boolean;
    maxCount?: number;
    onCountExceed?: (exceed: number) => void;
    disableUpload?: boolean;
    showUpload?: boolean;
    deletable?: boolean;
    deleteIcon?: ReactNode;
    capture?: InputHTMLAttributes<unknown>['capture'];
    onPreview?: (index: number, item: ImageUploadItem) => void;
    beforeUpload?: (file: File, files: File[]) => Promise<File | null> | File | null;
    upload: (file: File) => Promise<ImageUploadItem>;
    onDelete?: (item: ImageUploadItem) => boolean | Promise<boolean> | void;
    preview?: boolean;
    showFailed?: boolean;
    imageFit?: ImageProps['fit'];
    children?: ReactNode;
    renderItem?: (originNode: ReactElement, file: ImageUploadItem, fileList: ImageUploadItem[]) => ReactNode;
} & NativeProps<'--cell-size' | '--gap' | '--gap-vertical' | '--gap-horizontal'>;
export interface ImageUploaderRef {
    nativeElement: HTMLInputElement | null;
}
export declare const ImageUploader: React.ForwardRefExoticComponent<{
    defaultValue?: ImageUploadItem[] | undefined;
    value?: ImageUploadItem[] | undefined;
    columns?: number | undefined;
    onChange?: ((items: ImageUploadItem[]) => void) | undefined;
    onUploadQueueChange?: ((tasks: UploadTask[]) => void) | undefined;
    accept?: string | undefined;
    multiple?: boolean | undefined;
    maxCount?: number | undefined;
    onCountExceed?: ((exceed: number) => void) | undefined;
    disableUpload?: boolean | undefined;
    showUpload?: boolean | undefined;
    deletable?: boolean | undefined;
    deleteIcon?: ReactNode;
    capture?: InputHTMLAttributes<unknown>['capture'];
    onPreview?: ((index: number, item: ImageUploadItem) => void) | undefined;
    beforeUpload?: ((file: File, files: File[]) => Promise<File | null> | File | null) | undefined;
    upload: (file: File) => Promise<ImageUploadItem>;
    onDelete?: ((item: ImageUploadItem) => boolean | Promise<boolean> | void) | undefined;
    preview?: boolean | undefined;
    showFailed?: boolean | undefined;
    imageFit?: ImageProps['fit'];
    children?: ReactNode;
    renderItem?: ((originNode: ReactElement, file: ImageUploadItem, fileList: ImageUploadItem[]) => ReactNode) | undefined;
} & {
    className?: string | undefined;
    style?: (CSSProperties & Partial<Record<"--gap" | "--gap-vertical" | "--gap-horizontal" | "--cell-size", string>>) | undefined;
    tabIndex?: number | undefined;
} & React.AriaAttributes & React.RefAttributes<ImageUploaderRef>>;
export {};
