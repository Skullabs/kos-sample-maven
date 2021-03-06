project("Kos: Simple Project") {

    groupId = "io.skullabs.kos.sample"
    artifactId = "kos-sample-maven"
    version = "0.1.0-SNAPSHOT"

    // Versions
    val kotlinVersion = "1.3.61"
    val kosVersion = "0.1.0-SNAPSHOT"
    val vertxPluginVersion = "1.0.22"
    val logbackVersion = "1.2.3"
    
    // Configuration
    val launcherClass = "kos.core.Launcher"

    dependencies {
        // Kotlin Dependencies
        compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        
        // Kos Dependencies
        compile("io.skullabs.kos:kos-core")
        compile("io.skullabs.kos:kos-injector")
        provided("io.skullabs.kos:kos-annotations")
        
        // Logging
        compile("io.skullabs.kos:kos-logging-slf4j")
        compile("ch.qos.logback:logback-classic:${logbackVersion}")
    }

    dependencyManagement {
        dependencies {
            import("io.skullabs.kos:kos-bom:${kosVersion}")
        }
    }

    build {
        sourceDirectory = "src/main/kotlin"
        testSourceDirectory = "/src/test/kotlin"
        finalName = "application"

        plugins {
            /**
             * Configures Kotlin Plugin to perform usual compilation and annotation
             * processing. It will ensure any library that generates classes (like
             * Kos, AutoValue, Dagger) will be properly executed with Kotlin.
             */
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin:${kotlinVersion}") {
                executions {
                    execution(id = "kapt", goals = listOf("kapt"), phase = "generate-sources")
                    execution(id = "test-kapt", goals = listOf("test-kapt"), phase = "generate-test-sources")
                    execution(id = "compile", goals = listOf("compile"))
                    execution(id = "test-compile", goals = listOf("test-compile"))
                }
                configuration {
                    "jvmTarget" to "11"
                }
            }

            /**
             * Configures Java Compiler to be executed after Kapt runs, being able to
             * compile any source code generated by libraries (Kos in this case).
             */
            plugin("org.apache.maven.plugins:maven-compiler-plugin:3.8.1") {
                executions {
                    execution(id="default-compile", phase = "none")
                    execution(id="default-testCompile", phase = "none")

                    execution(id="java-compile", goals = listOf("compile"), phase = "compile")
                    execution(id="java-test-compile", goals = listOf("testCompile"), phase = "test-compile")
                }
                configuration {
                    "release" to "11"
                }
            }

            /**
             * Generates a Uber Jar that will run Kos as main Launcher.
             */
            plugin("org.apache.maven.plugins:maven-shade-plugin:3.2.0") {
                executions {
                    execution(id = "default-package", phase = "package", goals = listOf("shade")) {
                        configuration {
                            "createDependencyReducedPom" to true
                            "dependencyReducedPomLocation" to "\${project.build.directory}/pom-reduced.xml"
                            "transformers" {
                                "org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" {}
                                "org.apache.maven.plugins.shade.resource.ManifestResourceTransformer" {
                                    "manifestEntries" {
                                        "Main-Class" to launcherClass
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
