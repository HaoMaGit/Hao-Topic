// 自定义水球图样式
export function getWaterBallSVG(percent: number, color = '#4da6ff', size = 100) {
  const h = size;
  const w = size;
  const waterHeight = h * (1 - percent / 100);
  const waveY = waterHeight;
  const wave = `M0,${waveY} Q${w / 4},${waveY - 8} ${w / 2},${waveY} T${w},${waveY} L${w},${h} L0,${h} Z`;
  // 使用clipPath裁剪水波到圆形区域
  const svg = `<svg width="${w}" height="${h}" xmlns="http://www.w3.org/2000/svg">
    <defs>
      <clipPath id="clipCircle">
        <circle cx="${w / 2}" cy="${h / 2}" r="${w / 2 - 2}" />
      </clipPath>
    </defs>
    <circle cx="${w / 2}" cy="${h / 2}" r="${w / 2 - 2}" fill="#ebebeb" stroke="${color}" stroke-width="0"/>
    <g clip-path="url(#clipCircle)">
      <path d="${wave}" fill="${color}" fill-opacity="0.5"/>
    </g>
  </svg>`;
  return 'image://data:image/svg+xml;base64,' + window.btoa(unescape(encodeURIComponent(svg)));
}