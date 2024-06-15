plugins {
    id("java")
}

group = "org.joyboy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.opencsv:opencsv:5.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:2.17.1")
}

tasks.test {
    useJUnitPlatform()
}