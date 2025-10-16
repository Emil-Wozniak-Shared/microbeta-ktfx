plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "pl.ejdev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("pl.ejdev.medic")
    mainClass.set("pl.ejdev.medic.MedicApplication")
}
kotlin {
    jvmToolchain(17)
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing", "javafx.media")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") {
        exclude(group = "org.openjfx")
    }
    implementation("net.synedra:validatorfx:0.6.1") {
        exclude(group = "org.openjfx")
    }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    implementation("com.github.almasb:fxgl:17.3") {
        exclude(group = "org.openjfx")
        exclude(group = "org.jetbrains.kotlin")
    }

    implementation("com.hanggrian.ktfx:ktfx:0.3")

    // or download separately
    implementation("com.hanggrian.ktfx:ktfx-commons:0.3")
    implementation("com.hanggrian.ktfx:ktfx-layouts:0.3")
    implementation("com.hanggrian.ktfx:ktfx-coroutines:0.3")
    implementation("com.hanggrian.ktfx:ktfx-controlsfx:0.3")
    implementation("com.hanggrian.ktfx:ktfx-jfoenix:0.3")

    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.4.0")
    implementation("org.apache.xmlbeans:xmlbeans:5.1.1")
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("commons-io:commons-io:2.14.0")

    implementation("io.insert-koin:koin-core:3.5.3")

    // --- Testing ---
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testfx:testfx-core:4.0.18")
    testImplementation("org.testfx:testfx-junit5:4.0.18")
    testImplementation("org.hamcrest:hamcrest:3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()

    jvmArgs(
        // Open your module to TestFX
        "--add-opens", "pl.ejdev.medic/pl.ejdev.medic=org.testfx.junit5",
        "--add-opens", "pl.ejdev.medic/pl.ejdev.medic=ALL-UNNAMED",
        "--add-opens", "pl.ejdev.medic/pl.ejdev.medic.components=org.testfx.junit5",
        "--add-opens", "pl.ejdev.medic/pl.ejdev.medic.components=ALL-UNNAMED",

        // OPEN to org.testfx module specifically (for reflection)
        "--add-opens", "javafx.graphics/com.sun.javafx.application=org.testfx",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=org.testfx",

        // Export to org.testfx module
        "--add-exports", "javafx.graphics/com.sun.javafx.application=org.testfx",
        "--add-exports", "javafx.graphics/com.sun.glass.ui=org.testfx",

        // Open and export to ALL-UNNAMED as well
        "--add-opens", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=ALL-UNNAMED",
        "--add-exports", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED",
        "--add-exports", "javafx.graphics/com.sun.glass.ui=ALL-UNNAMED",

        // Additional opens for other JavaFX internals
        "--add-opens", "javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED",
        "--add-opens", "javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED"
    )
    // Optional: show detailed test logs
    testLogging {
        events("passed", "skipped", "failed")
    }
}

jlink {
    addExtraModulePath("pl.ejdev.merged.module")
    addExtraModulePath("kotlin.stdlib")
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
    jpackage {
        /**
         * On Arch `sudo pacman -S dpkg`
         */
        installerType = "deb" // "app-image", "exe", "msi", "rpm", "deb", "pkg", "dmg"
        jvmArgs = listOf("--add-reads=ktfx.layouts=kotlin.stdlib", "--add-reads=pl.ejdev.merged.module=kotlin.stdlib")
    }
}


