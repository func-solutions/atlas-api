# atlas-api
Инструмент для работы с конфигурациями.

## Подключение atlas-api
Для того чтобы использовать atlas-api, нам всего лишь
нужно добавить зависимость в BuildScript (build.gradle)

```groovy
repositories {
    mavenCentral()
    maven {
        url 'https://repo.c7x.dev/repository/maven-public/'
        credentials {
            username System.getenv("CRI_REPO_LOGIN")
            password System.getenv("CRI_REPO_PASSWORD")
        }
    }
}

dependencies {
    implementation 'me.func:atlas-api:1.0.0' // сама библиотека
}
```

## Использование atlas-api

Создадим класс, в котором сделаем примитивное использование
`atlas-api`.

