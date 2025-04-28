tasks.test {
    useJUnitPlatform() // JUnit 5 활성화
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("io.jsonwebtoken:jjwt:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.projectlombok:lombok:1.18.20")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.modelmapper:modelmapper:3.1.1'")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
}
