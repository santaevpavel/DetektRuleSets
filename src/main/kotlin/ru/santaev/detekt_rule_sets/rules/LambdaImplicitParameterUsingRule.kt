package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.TreeElementVisitor
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.allChildren
import ru.santaev.detekt_rule_sets.utils.IMPLICIT_PARAMETER_NAME
import ru.santaev.detekt_rule_sets.utils.line

class LambdaImplicitParameterUsingRule : Rule() {

    private val implicitParameterCounterVisitor = ImplicitParameterCounterVisitor()

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Use explicit parameter name of lambda",
        debt = Debt.FIVE_MINS
    )

    override fun visitLambdaExpression(lambdaExpression: KtLambdaExpression) {
        super.visitLambdaExpression(lambdaExpression)
        val hasSpecifiedParameter = lambdaExpression.functionLiteral.hasParameterSpecification()
        val implicitParameterUsingCount = implicitParameterCounterVisitor.let { visitor ->
            visitor.reset()
            lambdaExpression.accept(visitor, null)
            visitor.count
        }
        if (isMultiline(lambdaExpression) &&
            hasSpecifiedParameter.not() &&
            implicitParameterUsingCount > 0
        ) {
            report(createReport(lambdaExpression))
        }
    }

    private fun isMultiline(lambdaExpression: KtLambdaExpression): Boolean {
        val lBraceLine = lambdaExpression.leftCurlyBrace.line(lambdaExpression.containingKtFile)
        val rBraceLine = lambdaExpression.rightCurlyBrace?.line(lambdaExpression.containingKtFile)
        return rBraceLine != lBraceLine
    }

    private fun createReport(lambdaExpression: KtLambdaExpression): Finding {
        return CodeSmell(
            issue = issue,
            entity = Entity.from(lambdaExpression),
            message = "Use explicit parameter name of lambda"
        )
    }

    private class ImplicitParameterCounterVisitor: KtTreeVisitorVoid() {

        var count: Int = 0
            private set

        override fun visitReferenceExpression(expression: KtReferenceExpression) {
            super.visitReferenceExpression(expression)
            if (expression.text == IMPLICIT_PARAMETER_NAME) {
                count++
            }
        }

        fun reset() {
            count = 0
        }
    }
}