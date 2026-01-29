plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.gradleup.shadow") version "9.3.0"
    id("org.danilopianini.gradle-java-qa") version "1.161.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        // Java version used to compile and run the project
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val javaFXModules = listOf("base", "controls", "fxml", "swing", "graphics")

val supportedPlatforms = listOf("linux", "mac", "win") // All required for OOP

dependencies {
    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    // Example library: Guava. Add what you need (and use the latest version where appropriate).
    // implementation("com.google.guava:guava:28.1-jre")

    // JavaFX: comment out if you do not need them
    val javaFxVersion = "23.0.2"
    implementation("org.openjfx:javafx:$javaFxVersion")
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }
    
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    
    // Logging framework
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.slf4j:slf4j-api:2.0.7")
    
    // JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")
    
    // The BOM (Bill of Materials) synchronizes all the versions of Junit coherently.
    testImplementation(platform("org.junit:junit-bom:6.0.1"))
    // The annotations, assertions and other elements we want to have access when compiling our tests.
    testImplementation("org.junit.jupiter:junit-jupiter")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    // The engine that must be available at runtime to run the tests.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Guava Library
    implementation("com.google.guava:guava:33.5.0-jre")
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
    
    // Add JVM arguments to open Java modules for Gson reflection access
    jvmArgs(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "--add-opens", "java.base/java.nio.file=ALL-UNNAMED"
    )
}

val main: String by project

application {
    // Define the main class for the application
    mainClass.set(main)
}

// Disabilita Checkstyle per il set di sorgenti di test
tasks.named<Checkstyle>("checkstyleTest") {
    enabled = false
}

// Disabilita SpotBugs solo per i test
tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    if (name.contains("test", ignoreCase = true)) {
        enabled = false
    }
}