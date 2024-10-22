plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("kapt") version "1.9.10"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "ru.itmo"
version = "0.0.2"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Db
    implementation("org.postgresql:postgresql")

    // Run
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.liquibase:liquibase-core")
    implementation("org.mapstruct:mapstruct:1.6.0")
    kapt("org.mapstruct:mapstruct-processor:1.6.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.testcontainers:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.mockk:mockk:1.13.5")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    val bootJarTask = named("bootJar")

    val buildDockerImage by creating(Exec::class) {
        group = "docker"
        description = "Build Docker image for the Spring Boot application"

        dependsOn(bootJarTask)

        val projectName = project.name
        val projectVersion = project.version.toString()

        commandLine("docker", "build",
            "-t", "spring-highload:local",
            "--build-arg", "JAR_FILE=$projectName-$projectVersion.jar",
            ".")

    }
}
