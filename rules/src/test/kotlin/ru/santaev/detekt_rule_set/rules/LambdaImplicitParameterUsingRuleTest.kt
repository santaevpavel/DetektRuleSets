package ru.santaev.detekt_rule_set.rules

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Test

class LambdaImplicitParameterUsingRuleTest {

    private val rule = LambdaImplicitParameterUsingRule()

    @Test
    fun `should REPORT lambda implicit parameter using in multiline lambda`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should NOT REPORT lambda implicit parameter using in single line lambda`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = { it % 2 == 0 }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).isEmpty()
    }

    @Test
    fun `should NOT REPORT lambda implicit parameter using in lambda`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(iterator: Iterator<Int>): Boolean {
                        val lambda: (Int) -> Boolean = { number ->
                            number % iterator.next() == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).isEmpty()
    }

    @Test
    fun `should NOT REPORT lambda implicit parameter using in function`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(it: Int): Int {
                        return it + it
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).isEmpty()
    }

    @Test
    fun `should REPORT lambda implicit parameter using in function call with lambda`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(it: Int): Int {
                        getObservable()
                            .async()
                            .handleError()
                            .map {
                                it.isEmpty()
                            }
                            .subscribe()
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should NOT REPORT lambda implicit parameter using when it argument used in nested lambda`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(it: Int): Int {
                        getObservable()
                            .async()
                            .handleError()
                            .map { 
                                listOf("a", "b", "c").map { it.length } 
                            }
                            .subscribe()
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).isEmpty()
    }
}