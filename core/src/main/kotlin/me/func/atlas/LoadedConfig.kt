package me.func.atlas

import org.bukkit.configuration.file.FileConfiguration

data class LoadedConfig(
    val fileName: String,
    val url: String,
    val configuration: FileConfiguration
)