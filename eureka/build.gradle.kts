plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server:4.1.3")

    // Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-config:4.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:4.1.4")
    implementation(kotlin("stdlib"))

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

        commandLine("docker", "build",
            "-t", "eureka:local",
            "--build-arg", "JAR_FILE=$projectName.jar",
            ".")

    }
}