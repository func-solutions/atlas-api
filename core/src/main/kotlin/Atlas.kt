import org.bukkit.plugin.java.JavaPlugin

object Atlas {

    // Получение объекта главного класса плагина
    val provided: JavaPlugin = JavaPlugin.getProvidingPlugin(this.javaClass)

}