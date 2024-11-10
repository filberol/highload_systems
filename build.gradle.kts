repositories {
    mavenCentral()
}

// Move to serivces with deps
//tasks.jacocoTestReport {
//    dependsOn(tasks.test)
//    classDirectories.setFrom(
//        sourceSets.main.get().output.asFileTree.matching {
//            exclude(
//                "**/generated/**",
//                "ru/itmo/highload_systems/*.class",
//                "ru/itmo/highload_systems/controller/ControllerExceptionHandler.class"
//            )
//        }
//    )
//    reports {
//        xml.required.set(true)
//        xml.outputLocation.set(file("${layout.buildDirectory.get().asFile.path}/jacoco/jacoco.xml"))
//
//        csv.required.set(true)
//        csv.outputLocation.set(file("${layout.buildDirectory.get().asFile.path}/jacoco/jacoco.csv"))
//    }
//}
