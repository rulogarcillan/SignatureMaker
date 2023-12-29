
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.googleKsp) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics.gradle)
    alias(libs.plugins.detekt)
}

subprojects{

    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        buildUponDefaultConfig = true // preconfigure defaults
        autoCorrect = true
        allRules = false
        config.setFrom(File("$projectDir/../config/detekt-config.yml")) // point to your custom config defining rules to run, overwriting default behavior
    }

    dependencies {
        detektPlugins(rootProject.libs.detekt.formatting)
        detektPlugins(rootProject.libs.detekt.compose.rules)
        detektPlugins(rootProject.libs.detekt.compose.rules.kode)
    }
}



