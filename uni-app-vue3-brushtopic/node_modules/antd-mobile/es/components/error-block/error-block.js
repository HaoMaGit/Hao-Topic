import { createErrorBlock } from './create-error-block';
import { busyImage, defaultImage, disconnectedImage, emptyImage } from './images';
const imageRecord = {
  'default': defaultImage,
  'disconnected': disconnectedImage,
  'empty': emptyImage,
  'busy': busyImage
};
export const ErrorBlock = createErrorBlock(imageRecord);