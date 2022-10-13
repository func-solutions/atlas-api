package me.func.atlas

import me.func.atlas.util.fileLastName
import me.func.atlas.util.warn
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.concurrent.CompletableFuture

const val LOCAL_DIR_NAME = "atlas"

object Atlas {

    @JvmStatic
    @JvmOverloads
    fun download(fileUrl: String, saveDir: String = LOCAL_DIR_NAME): File? {
        return try {
            val dir = Paths.get(saveDir)
            if (Files.notExists(dir))
                Files.createDirectory(dir)

            val website = URL(fileUrl)
            val file = File(saveDir + "/" + fileUrl.fileLastName())
            file.createNewFile()
            website.openStream().use { `in` ->
                Files.copy(
                    `in`,
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

            return@supplyAsync LoadedConfig(file.nameWithoutExtension, config)
        }
    }

    @JvmStatic
    fun config(fileUrl: Collection<String>) = fileUrl.map { config(it) }

}