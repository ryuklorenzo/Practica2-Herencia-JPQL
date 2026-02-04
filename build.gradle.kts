plugins {
    id("java")
}

group = "com.alp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JPA + Hibernate
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    // PostgreSQL driver
    runtimeOnly("org.postgresql:postgresql:42.7.3")
}

tasks.test {
    useJUnitPlatform()
}