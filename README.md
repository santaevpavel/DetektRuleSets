# DetektRuleSets

Extra `detekt` rules for supporting `Kotlin Coding Convention` and `Android Kotlin Style`.    

# Getting started

## How to add rules to your project

1. `Detekt` should be set up in your project.
1. Add `jitpack` maven repository. 
    ```
    maven { url "https://jitpack.io" }`
    ```
1. Add dependency to your project. 
    ```
    detektPlugins com.github.santaevpavel:detekt-rule-set:{version}
    ```
1.  Add exclude rule to `detekt` config file to let `detekt` don't warn via config validation.
    ```
    config:
      validation: true
      # Add this line below
      excludes: "extrarules,.*>.*>*"
    ```