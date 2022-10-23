package me.func.atlas

import me.func.atlas.util.fileLastName
import me.func.atlas.util.log
import me.func.atlas.util.warn
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture

const val LOCAL_DIR_NAME = "atlas"

object Atlas {

    private val configs = hashMapOf<String, LoadedConfig>() // file name to config

    @JvmStatic
    @JvmOverloads
    fun download(fileUrl: String, saveDir: String = LOCAL_DIR_NAME, timeout: Int = 3_000, cache: Boolean = true): File? {
        return try {
            val dir = Paths.get(saveDir)
            if (Files.notExists(dir))
                Files.createDirectory(dir)

            val website = URL(fileUrl)
            val file = File(saveDir + "/" + fileUrl.fileLastName())
            file.createNewFile()

            website.openConnection().apply {
                connectTimeout = 1_500
                readTimeout = timeout
                useCaches = cache
            }.getInputStream().use {
                Files.copy(
                    it,
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }

            file
        } catch (exception: Exception) {
            warn(exception.message ?: "Download failure! File: $fileUrl, directory: $saveDir")
            null
        }
    }

    @JvmStatic
    fun config(fileUrl: String): CompletableFuture<LoadedConfig> {
        return CompletableFuture.supplyAsync {
            val file = download(fileUrl) ?: throw RuntimeException("No file found!")
            val config = YamlConfiguration.loadConfiguration(file)
            val defaultConfig = YamlConfiguration.loadConfiguration(InputStreamReader(file.inputStream()))

            config.setDefaults(defaultConfig)

            val loaded = LoadedConfig(file.nameWithoutExtension, fileUrl, config)

            configs[file.nameWithoutExtension] = loaded

            return@supplyAsync loaded
        }
    }

    @JvmStatic
    fun config(fileUrl: Collection<String>) = fileUrl.map { config(it) }

    @JvmOverloads
    @JvmStatic
    fun update(onEnd: Runnable = Runnable { }) {
        CompletableFuture.allOf(*config(configs.values.map { it.url }.toList()).toTypedArray())
            .thenAccept { onEnd.run() }
    }

    @JvmStatic
    fun find(key: String): FileConfiguration {

        val value = configs[key]

        if (value != null) return value.configuration

        warn("Error! Not key matching, all keys:\n")

        configs.keys.forEach { log(" + $it") }

        throw RuntimeException("Config not found! $key")
    }

    @JvmStatic
    fun section(key: String, path: String) = find(key).getConfigurationSection(path).getKeys(false)

}