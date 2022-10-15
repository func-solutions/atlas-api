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
    implementation 'me.func:atlas-api:1.0.9' // сама библиотека
}
```

## Использование atlas-api

```kotlin
Atlas.config( // скачиваем web-config
    "https://storage.c7x.dev/func/tycoon/config/ore.yml",
).thenAccept { file ->
    println("Loaded! " + file.fileName)
    
    file.configuration // объект нашей конфигурации
}
```

ИЛИ

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

```kotlin
Atlas.find("ore") // получение конфигурации

Atlas.section("ore", "data") // получение секции по имени data и названию конфигурации ore

Atlas.update() // обновляет все загруженные конфиги

Atlas.update {
    log("Конфигурация обновилась!")
}
```