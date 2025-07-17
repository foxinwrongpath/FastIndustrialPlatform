import io.gitlab.gregtechlite.magicbookgradle.utils.modGroup
import io.gitlab.gregtechlite.magicbookgradle.utils.modId

plugins {
    `maven-publish`
    id("io.gitlab.gregtechlite.magicbookgradle") version "1.0.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

magicbook {
    includeWellKnownRepositories()
    usesJUnit()
    java {
        enableModernJavaSyntax()
        withSourcesJar()
    }

    mod {
        enabledJava17RunTasks = true
        useAccessTransformers()
        usesMixin()
        generateGradleTokenClass = "$modGroup.$modId.ExampleTags"
    }
}

minecraft {
    mcpMappingChannel.set("stable")
    mcpMappingVersion.set("39")
}

dependencies {
    compileOnlyApi("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.jetbrains:annotations:24.1.0")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}
