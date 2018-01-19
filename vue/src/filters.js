// Vue.js カスタムフィルタ

/**
 * カンマ付円表示する
 */
export function currency (price) {
  return price.toString().replace(/(\d)(?=(\d{3})+$)/g, '$1,') + ' 円'
}
