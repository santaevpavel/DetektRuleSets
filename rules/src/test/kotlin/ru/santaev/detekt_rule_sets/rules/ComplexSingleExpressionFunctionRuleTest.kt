package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Test

class ComplexSingleExpressionFunctionRuleTest {

    private val rule = ComplexSingleExpressionFunctionRule()

    @Test
    fun `should find multiline single expression`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean = Factory.instance()
                        .create(this)
                        .asBoolean()
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find multiple calls that large then max number of calls`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean = Factory.instance().create(this).asBoolean()
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find nested multiple calls that large then max number of calls`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean = Factory.instance(memberFunction(this.otherFunction())
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should NOT find multiple calls that small then max number of calls`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean = Factory.instance().create(this)
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find simple raw string as single expression function`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): String = "Simple string"
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find simple single line single expression function`() {
        val code =
            """
                class Foo : Serializable() {

                    fun tooooooLongFunctionName() =
                        "TooooooLongString ${'$'}LongVariable1 ${'$'}LongVariable2"
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue on function with block body`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar() : String {
                        return buildString {
                            append("Bar")
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }
}