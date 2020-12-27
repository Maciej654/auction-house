plugins {
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("io.freefair.lombok") version "5.3.0"
    application
}

group = "pl.poznan.put"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("http://sandec.bintray.com/repo")
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
    version = "11.0.1"
    modules = javafxModuleNames.map { "javafx.$it" }
}

application {
    mainClass.set("pl.poznan.put.Main")
}

dependencies {
    // javafx
    implementation("org.openjfx:javafx:${javafx.version}")
    javafxModuleNames.forEach {
        implementation("org.openjfx:javafx-$it:${javafx.version}")
    }

    // hibernate
    implementation("org.hibernate:hibernate-core:5.4.26.Final")

    // markdown support
    implementation("com.sandec:mdfx:0.1.6")

    // logging
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.wrapper {
    gradleVersion = "6.7.1"
}