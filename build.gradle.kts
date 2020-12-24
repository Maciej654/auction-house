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
    implementation("org.openjfx:javafx:${javafx.version}")
    javafxModuleNames.forEach {
        implementation("org.openjfx:javafx-$it:${javafx.version}")
    }
//    val lombok = "org.projectlombok:lombok:1.18.16"
//    compileOnly(lombok)
//    annotationProcessor(lombok)

    implementation("com.sandec:mdfx:0.1.6")
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

tasks.wrapper {
    gradleVersion = "6.7.1"
}