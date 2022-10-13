package me.func.atlas

import org.bukkit.configuration.file.FileConfiguration

data class LoadedConfig(
    val fileName: String,
    val configuration: FileConfiguration
)