import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.0"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
}

group = "de.welcz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("io.arrow-kt:arrow-core:1.1.2")
  implementation("io.github.microutils:kotlin-logging:2.1.23")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("io.kotest:kotest-assertions-json")
  testImplementation("io.kotest:kotest-property")
  testImplementation("io.kotest:kotest-framework-datatest")
  testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.1")
  testImplementation("com.ninja-squad:springmockk:3.1.1")
}

dependencyManagement {
  imports {
    mavenBom("io.kotest:kotest-bom:5.3.0")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
