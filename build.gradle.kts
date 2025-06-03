buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1") // use latest stable version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10") // match Kotlin version
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
