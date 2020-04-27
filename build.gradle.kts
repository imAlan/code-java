plugins {
    java
}

group = "com.nyc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.lmax:disruptor:3.4.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}