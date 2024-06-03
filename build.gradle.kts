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
    implementation("org.openpnp:opencv:4.9.0-0")

}

tasks.test {
    useJUnitPlatform()
}