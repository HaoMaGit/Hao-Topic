import type { FC, ReactNode } from 'react';
import { TaskStatus } from './image-uploader';
import type { ImageProps } from '../image';
declare type Props = {
    onClick?: () => void;
    onDelete?: () => void;
    deletable: boolean;
    deleteIcon: ReactNode;
    url?: string;
    file?: File;
    status?: TaskStatus;
    imageFit: ImageProps['fit'];
};
declare const PreviewItem: FC<Props>;
export default PreviewItem;
