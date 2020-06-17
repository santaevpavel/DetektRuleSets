package ru.santaev.detekt_rule_sets.utils

private const val DEBUG = false

@Suppress("ConstantConditionIf")
fun log(message: String) {
    if (DEBUG) {
        println(message)
    }
}