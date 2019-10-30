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
    fun `should not find multiple calls that small then max number of calls`() {
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
    fun `should not find simple raw string as single expression function`() {
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
    fun `should not find simple single line single expression function`() {
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
}