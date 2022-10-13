dependencies {
    compileOnly("cristalix:bukkit-core:21.01.30")
    compileOnly("cristalix:dark-paper:21.02.03")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "atlas-api"
            from(components["java"])
        }
    }
}