plugins {
    id("org.openjfx.javafxplugin") version "0.0.8"
    application
}

group = "pl.poznan.put"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

val javafxModuleNames = listOf(
    "controls",
    "graphics",
    "fxml"
)

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

javafx {
    version = "11"
    modules = javafxModuleNames.map { "javafx.$it" }
}

application {
    mainClass.set("pl.poznan.put.Main")
}

dependencies {
    implementation("org.openjfx:javafx:${javafx.version}")
    javafxModuleNames.forEach {
        implementation("org.openjfx:javafx-$it:${javafx.version}")
    }
    implementation("org.projectlombok:lombok:1.18.16")
}

tasks.wrapper {
    gradleVersion = "6.7.1"
}