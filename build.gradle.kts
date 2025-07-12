plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "glass.yasan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.openai.java)
    testImplementation(libs.kotlin.test)
}

kotlin {
    jvmToolchain(jdkVersion = 17)
}

tasks.test {
    useJUnitPlatform()
}