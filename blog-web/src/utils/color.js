export function randomColor() {
  const h = Math.floor(Math.random() * 360);
  const s = 50 + Math.floor(Math.random() * 40);  // 饱和度 50-90%，增强渐变幅度
  const l = 60 + Math.floor(Math.random() * 30);  // 亮度 60-90%，增强渐变幅度
  const a = 0.7;  // 透明度 0.7，稍微增强
  return `hsla(${h}, ${s}%, ${l}%, ${a})`;
}
