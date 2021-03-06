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
    "fxml",
    "web"
)

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

javafx {
    version = "14.0.1"
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

    // logging
    implementation("org.slf4j:slf4j-log4j12:1.7.30")

    // utils
    implementation("org.apache.commons:commons-lang3:3.0")
    implementation("commons-validator:commons-validator:1.7")

    // oracle driver
    implementation("com.oracle.database.jdbc:ojdbc10:19.9.0.0")
}

tasks.wrapper {
    gradleVersion = "6.7.1"
}