# bento
福井本社側のお弁当注文するシステムです。

## 事前条件

- [Cloud SDK](https://cloud.google.com/sdk/?hl=ja)
- [Java8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
- Google Cloud SDK Java Extensions

  ```sh
  gcloud components install app-engine-java`
  ```
  を実行する。

## バックエンド

- Spring Boot

## フロントエンド

- Vue.js
  - `<script src="https://unpkg.com/vue"></script>`

フロント側の実行
```sh
cd vue
npm i
npm run dev
```

## ビルド

```sh
./gradlew build
```

## ローカルで実行

```sh
./gradlew appengineRun
```
を実行して http://localhost:8080/ をブラウザで開く。
