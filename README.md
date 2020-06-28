# DetektRuleSets

[![](https://jitpack.io/v/santaevpavel/detekt-rule-set.svg)](https://jitpack.io/#santaevpavel/detekt-rule-set)

Extra `detekt` rules for supporting `Kotlin Coding Convention` and `Android Kotlin Style`.    

# Getting started

## How to add rules to your project

1. `Detekt` should be set up in your project.
1. Add `jitpack` maven repository. 
    ```
    repositories {
        maven { url "https://jitpack.io" }
    }
    ```
1. Add dependency to your project. 
    ```
    dependencies {
        detektPlugins "com.github.santaevpavel:detekt-rule-set:{version}"
    }
    ```
1.  Add exclude rule to `detekt` config file to let `detekt` don't warn via config validation.
    ```
    config:
      validation: true
      # Add this line below
      excludes: "extrarules,.*>.*>*"
    ```