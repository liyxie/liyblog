export function randomColor() {
  const h = Math.floor(Math.random() * 360);
  const s = 60 + Math.floor(Math.random() * 20);  // 饱和度 60-80%
  const l = 75 + Math.floor(Math.random() * 15);  // 亮度 75-90%
  const a = 0.6;  // 透明度 0.6
  return `hsla(${h}, ${s}%, ${l}%, ${a})`;
}
