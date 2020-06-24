package ru.santaev.detekt_rule_set.rules

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Test

class NoMemberExtensionRuleTest {

    private val rule = NoMemberExtensionRule()

    @Test
    fun `should find class member extension function`() {
        val code =
            """
                class Foo : Serializable() {

                    fun memberFunction(): Boolean {
                        return true
                    }

                    fun String.extensionFunction(): Boolean {
                        return false
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find class companion extension function`() {
        val code =
            """
                class Foo : Serializable() {

                    companion object {
                        fun String.extensionFunction(): Boolean {
                            return false
                        }
                    }

                    fun memberFunction(): Boolean {
                        return true
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should not find top level extension functions`() {
        val code =
            """
                fun String.topLevelExtensionFunction(): Boolean {
                    return true
                }

                fun String.Companion.topLevelExtensionFunction(): Boolean {
                    return true
                }

                class Foo : Serializable() {

                    fun memberFunction(): Boolean {
                        return true
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }
}