plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "glass.yasan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}