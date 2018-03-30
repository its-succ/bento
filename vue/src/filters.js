// Vue.js カスタムフィルタ
import moment from 'moment'
moment.locale('ja')

/**
 * カンマ付円表示する
 */
export function currency (price) {
  return price.toString().replace(/(\d)(?=(\d{3})+$)/g, '$1,') + ' 円'
}
/**
 * 曜日を表示する
 */
export function dayofweek (date) {
  return moment(date).format('ddd')
}
