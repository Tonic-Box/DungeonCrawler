plugins {
    id("java")
    application
}

group = "com.tonic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

val gdxVersion = "1.11.0"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Core LibGDX dependency
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")

    // Desktop backend for LibGDX (using LWJGL3)
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")

    // Platform-specific natives for desktop
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
}

tasks.test {
    useJUnitPlatform()
}