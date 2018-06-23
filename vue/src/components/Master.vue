<template lang="html">
  <div class="master">
    <navigation></navigation>
    <div id="content-view" v-if="masters !== undefined">
      <div class="doc-container" >
        <q-radio 
          v-model="kind" 
          val="okazu" 
          label="おかずマスタ" 
          color="light-blue"
        />
        <q-radio 
          v-model="kind" 
          val="gohan" 
          label="ごはんマスタ" 
          color="cyan"
        />
      </div>
      <div class="doc-container">
        <table class="q-table bordered vertical-separator">
          <thead class="bg-primary text-white">
            <tr class="text-center">
              <th>名称</th>
              <th>値</th>
              <th>値段</th>
              <th v-if="kind === 'okazu'">曜日</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="v in $v.masters[kind].$each.$iter">
              <td>
                <q-field :error="!v.label.required" error-label="名称は必須入力です">
                  <q-input v-model.trim="v.label.$model" :error="v.label.$error" @blur="v.label.$touch"/>
                </q-field>
              </td>
              <td>
                <q-field :error="!v.value.required" error-label="値は必須入力です">
                  <q-input v-model="v.value.$model" :readonly="v.id.$model !==null" :error="v.value.$error" @blur="v.value.$touch" />
                </q-field>
              </td>
              <td>
                <q-field :error="!v.price.required" error-label="値段は必須入力です">
                  <q-input v-model.trim="v.price.$model" align="right" type="number" suffix="円" :error="v.price.$error" @blur="v.price.$touch" />
                </q-field>
              </td>
              <td v-if="kind === 'okazu'">
                <q-select
                  v-model="v.dayofweek.$model"
                  inverted
                  color="cyan"
                  separator
                  :options="dayofweek_options"
                />
              </td>
              <td>
                <q-btn glossy round color="black" icon="delete" @click="remove(v.$model)"/>
              </td>
            </tr>
            <tr>
              <td></td>
              <td></td>
              <td></td>
              <td v-if="kind === 'okazu'"></td>
              <td><q-btn glossy round color="secondary" icon="add" size="sm" @click="add()" /></td>
            </tr>
          </tbody>
        </table>
        <div>
          <q-btn glossy rounded color="primary" @click="submitMaster()">更新する</q-btn>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navigation from './Navigation'
import {
  Toast,
  Loading,
  QField,
  QInput,
  QSelect,
  QRadio,
  QBtn
} from 'quasar'
import { required } from 'vuelidate/lib/validators'

export default {
  components: {
    Navigation,
    QField,
    QInput,
    QSelect,
    QRadio,
    QBtn
  },
  name: 'master',
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      kind: 'okazu',
      masters: {
        okazu: [],
        gohan: []
      },
      dayofweek_options: [
        { label: '', value: null },
        { label: '月', value: 1 },
        { label: '火', value: 2 },
        { label: '水', value: 3 },
        { label: '木', value: 4 },
        { label: '金', value: 5 }
      ],
      initmaster: {
        okazu: {
          id: null,
          label: '',
          value: '',
          price: 0,
          dayofweek: null
        },
        gohan: {
          id: null,
          label: '',
          value: '',
          price: 0
        }
      }
    }
  },
  validations: {
    masters: {
      okazu: {
        required,
        $each: {
          id: {
          },
          label: {
            required
          },
          value: {
            required
          },
          price: {
            required
          },
          dayofweek: {
          }
        }
      },
      gohan: {
        required,
        $each: {
          id: {
          },
          label: {
            required
          },
          value: {
            required
          },
          price: {
            required
          }
        }
      }
    }
  },
  methods: {
    /**
     * 登録済みのマスタを取得する
     */
    async getMasters () {
      if (this.user.id === undefined) {
        // user未設定時はNOP
        return
      }
      try {
        const response = await this.$http.get(`api/masters/all`)
        Object.keys(response.data).forEach(key => {
          this.masters[key] = response.data[key]
        })
      }
      catch (error) {
        console.error(error)
      }
    },
    /**
     * 指定のマスタを1行追加します
     */
    add () {
      let copied = Object.assign({}, this.initmaster[this.kind])
      this.masters[this.kind].push(copied)
    },
    /**
     * 指定のマスタを削除します
     */
    remove (target) {
      this.masters[this.kind] = this.masters[this.kind].filter(function (m) {
        return m !== target
      })
    },
    /**
     * 指定のAPIに指定の内容をPOSTします。
     */
    async submitMaster () {
      this.$v.$touch()
      if (!this.$v.masters[this.kind].required) {
        this.showErrorToast('マスタを空にはできません。')
        return
      }
      if (this.$v.masters[this.kind].$error) {
        this.showErrorToast('エラーのあるマスタがあります。')
        return
      }
      Loading.show()
      try {
        const response = await this.$http.post(`api/masters/${this.kind}/update`, this.masters[this.kind])
        this.masters[this.kind] = response.data
        Loading.hide()
        Toast.create.positive({
          html: 'マスタを更新しました。'
        })
      }
      catch (error) {
        console.error(error)
        this.showErrorToast('マスタの更新に失敗しました。')
      }
      finally {
        if (Loading.isActive) Loading.hide()
      }
    },
    /**
     * Toastでエラー表示する
     */
    showErrorToast (message) {
      Toast.create.negative({
        html: message
      })
    }
  },
  mounted () {
    this.getMasters()
  }
}
</script>

<style lang="css">
</style>
