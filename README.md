# WebApplication

## Package
```
./gradlew distZip
```

## Run
```shell
$ apt install jo openssl
$ jo -p salt=$(openssl rand -hex 128) > settings.json
```