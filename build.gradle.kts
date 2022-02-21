import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.zendesk"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.2.4"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.zendesk.horoscopes.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-junit5-web-client:4.0.0-milestone4")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-web-openapi")
  implementation("io.vertx:vertx-hazelcast:3.9.0")
  implementation("io.vertx:vertx-pg-client:4.2.4")
  implementation("org.apache.logging.log4j:log4j-core:2.17.1")
  implementation("org.apache.logging.log4j:log4j-api:2.17.1")
  implementation("org.jboss.resteasy:resteasy-vertx:6.0.0.Final")
  implementation("junit:junit:4.13.1")
  implementation("org.jboss.resteasy:resteasy-jaxrs:3.15.3.Final")
  implementation("com.zandero:rest.vertx:1.0.6.1")
  runtimeOnly("io.netty:netty-resolver-dns-native-macos:XXX:osx-x86_64")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
  testImplementation("org.mockito:mockito-core:4.3.1")
  testImplementation("io.vertx:vertx-unit:4.2.5")

}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf(
    "run",
    mainVerticleName,
    "--redeploy=$watchForChange",
    "--launcher-class=$launcherClassName",
    "--on-redeploy=$doOnChange"
  )
}
