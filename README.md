# Приложение Совместное финансирование

## Использованные технологии
- Kotlin
- http4k
- Docker

## Run
```shell
docker run -d -e SALT=$(openssl rand -hex 128) -p 9000:9000 ghcr.io/tyumentsev4/crowdfunding:master
```
