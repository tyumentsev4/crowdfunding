# Приложение Совместное финансирование

## Использованные технологии
- Kotlin
- http4k
- Docker
```
./gradlew distZip
```

## Run
```shell
docker run -d -e SALT=$(openssl rand -hex 128) -p 9000:9000 registry.gitlab.com/tyumentsev4/crowdfunding_kt_lab:1.0.0
```
