package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Assert.*
import org.junit.Test

class ClassMembersOrderRuleTest {

    private val rule = ClassMembersOrderRule(Config.empty)

    @Test
    fun `should not find any issues with default config`() {
        val code =
            """
                class Foo : Serializable {

                    companion object {
                        private const val DEFAULT_VALUE = 3
                    }

                    val value: Int = 3
                    private var count: Int = 3

                    fun bar(): Boolean {
                        return true
                    }

                    private fun bar2(): Int {
                        return if (bar()) 3 else 2
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should find companion object in bottom of class`() {
        val code =
            """
                class Foo : Serializable {

                    val value: Int = 3
                    private var count: Int = 3

                    fun bar(): Boolean {
                        return true
                    }

                    private fun bar2(): Int {
                        return if (bar()) 3 else 2
                    }

                    companion object {
                        private const val DEFAULT_VALUE = 3
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }
}