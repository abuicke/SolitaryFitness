pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://s01.oss.sonatype.org/content/repositories/releases/")
    }
}

rootProject.name = "SolitaryFitness"
include(":app")

// include(":caimito")
// project(":caimito").projectDir = File("..\\caimito\\caimito")