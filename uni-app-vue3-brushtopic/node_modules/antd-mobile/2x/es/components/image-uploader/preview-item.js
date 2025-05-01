import React, { useEffect, useMemo } from 'react';
import classNames from 'classnames';
import Image from '../image';
import SpinLoading from '../spin-loading';
import { useConfig } from '../config-provider';
const classPrefix = `adm-image-uploader`;
const PreviewItem = props => {
  const {
    locale
  } = useConfig();
  const {
    url,
    file,
    deletable,
    deleteIcon,
    onDelete,
    imageFit
  } = props;
  const src = useMemo(() => {
    if (url) {
      return url;
    }
    if (file) {
      return URL.createObjectURL(file);
    }
    return '';
  }, [url, file]);
  useEffect(() => {
    return () => {
      if (file) URL.revokeObjectURL(src);
    };
  }, [src, file]);
  function renderLoading() {
    return props.status === 'pending' && React.createElement("div", {
      className: `${classPrefix}-cell-mask`
    }, React.createElement("span", {
      className: `${classPrefix}-cell-loading`
    }, React.createElement(SpinLoading, {
      color: 'white'
    }), React.createElement("span", {
      className: `${classPrefix}-cell-mask-message`
    }, locale.ImageUploader.uploading)));
  }
  function renderDelete() {
    return deletable && React.createElement("span", {
      className: `${classPrefix}-cell-delete`,
      onClick: onDelete
    }, deleteIcon);
  }
  return React.createElement("div", {
    className: classNames(`${classPrefix}-cell`, props.status === 'fail' && `${classPrefix}-cell-fail`)
  }, React.createElement(Image, {
    className: `${classPrefix}-cell-image`,
    src: src,
    fit: imageFit,
    onClick: props.onClick
  }), renderLoading(), renderDelete());
};
export default PreviewItem;