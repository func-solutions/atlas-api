# atlas-api
Инструмент для работы с web-конфигурациями.

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
    implementation 'me.func:atlas-api:1.0.6' // сама библиотека
}
```

## Использование atlas-api

Единственное скачивание
```kotlin
Atlas.config(
    "https://storage.c7x.dev/func/tycoon/config/ore.yml",
).thenAccept { file ->
    println("Loaded! " + file.fileName)
    
    file.configuration // объект нашей конфигурации
}
```

Множественное скачивание
```kotlin
Atlas.config(
    listOf(
        "https://storage.c7x.dev/func/tycoon/config/ore.yml",
        "https://storage.c7x.dev/func/tycoon/config/buildings.yml",
        "https://storage.c7x.dev/func/tycoon/config/chests.yml"
    )
).forEach { future ->
    future.thenAccept { file ->
        println("Loaded! " + file.fileName)
        
        file.configuration // объект нашей конфигурации
    }
}
```

