package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Before
import org.junit.Test

class NoCommentedCodeRuleTest {

    private lateinit var rule: NoCommentedCodeRule

    @Before
    fun setup() {
        rule = NoCommentedCodeRule(Config.empty)
    }

    @Test
    fun `should find issue if single line code commented in function (local variable declaration) and allowToDoFixIt enabled`() {
        rule = NoCommentedCodeRule(
            TestConfig(
                "allowToDoAndFixIt" to "true"
            )
        )
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        // val a = 0
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find issue if single line code commented in function (local variable declaration) and allowToDoFixIt disabled`() {
        rule = NoCommentedCodeRule(
            TestConfig(
                "allowToDoAndFixIt" to "false"
            )
        )
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        // val a = 0
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find issue if single line code commented in function (function call)`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        // bar("lambda").foo("A")
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find issue if multiline code commented in function 1`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        /* val a = 0
                        foo() */
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find issue if multiline code commented in function 2`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        /*
                        lambda = { }
                        */
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find issue if function commented in class`() {
        val code =
            """
                class Foo : Serializable() {

                    /* fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    } */
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find issue if function commented in file`() {
        val code =
            """
                class Foo : Serializable() {
                }

                fun foo() = 2

                /* fun bar(): Boolean {
                    val lambda: (Int) -> Boolean = {
                    it % 2 == 0
                 } */
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should find issue if comment contains FIXIT, but option allowToDoAndFixIt is disabled`() {
        rule = NoCommentedCodeRule(
            TestConfig(
                "allowToDoAndFixIt" to "false"
            )
        )
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        /* FIXIT
                        val a = 0
                        bar()
                        */
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(1)
    }

    @Test
    fun `should NOT find issue if there is function java doc comment`() {
        val code =
            """
                class Foo : Serializable() {

                    /**
                     * Test java doc
                     */
                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue if there is class java doc comment`() {
        val code =
            """
                /**
                 * Test java doc
                 */
                class Foo : Serializable() {


                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue if comment contains TODO`() {
        rule = NoCommentedCodeRule(
            TestConfig(
                "allowToDoAndFixIt" to "true"
            )
        )
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        /* TODO
                        val a = 0
                        bar()
                        */
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue if comment contains FIXIT`() {
        rule = NoCommentedCodeRule(
            TestConfig(
                "allowToDoAndFixIt" to "true"
            )
        )
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                        /* FIXIT
                        val a = 0
                        bar()
                        */
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue if commented not code (regular text 1)`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        // This lambda checks number ...
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue if commented not code (regular text 2)`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        /*
                         Do not call function fooBar() after lambda call.
                         Use other function, for example MyClass.kt to pass other parameters.
                         */
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue if commented not code (regular text 3)`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        /*
                         Call other function bar() to check ...
                        */
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }

    @Test
    fun `should NOT find issue if commented not code (url)`() {
        val code =
            """
                class Foo : Serializable() {

                    fun bar(): Boolean {
                        // http://stackoverflow.com/question/1
                        val lambda: (Int) -> Boolean = {
                            it % 2 == 0
                        }
                    }
                }
            """
        val findings = rule.lint(code)

        assertThat(findings).hasSize(0)
    }
}

