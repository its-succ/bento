# bento

## セットアップ
1. グローバルに`firebase-tools`をインストールします。
    ```sh
    yarn global add firebase-tools
    ```
1. `firebase-tools`でログインします。
    ```sh
    firebase login
    ```
1. プロジェクトを設定します。
    ```sh
    firebase use --add
    ```

## 開発
### 依存モジュールをインストールする
```sh
yarn install
```

### Compiles and hot-reloads for development
```
yarn run serve
```

### Compiles and minifies for production
```
yarn run build
```

### Run your tests
```
yarn run test
```

### Lints and fixes files
```
yarn run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

## デプロイ
```sh
firebase deploy
```
