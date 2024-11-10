plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("kapt") version "1.9.10"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
    id("jacoco")
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

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Libs
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.liquibase:liquibase-core")
    implementation("org.mapstruct:mapstruct:1.6.0")
    kapt("org.mapstruct:mapstruct-processor:1.6.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-config:4.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.1.4")

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

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(
                "**/generated/**",
                "ru/itmo/highload_systems/*.class",
                "ru/itmo/highload_systems/controller/ControllerExceptionHandler.class"
            )
        }
    )
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("${layout.buildDirectory.get().asFile.path}/jacoco/jacoco.xml"))

        csv.required.set(true)
        csv.outputLocation.set(file("${layout.buildDirectory.get().asFile.path}/jacoco/jacoco.csv"))
    }
}