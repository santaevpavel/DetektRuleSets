package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*
import ru.santaev.detekt_rule_sets.utils.line

class ComplexSingleExpressionFunctionRule : Rule() {

    companion object {
        private const val MAX_CALL_CHAINS_IN_SINGLE_EXPRESSION = 2
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Use only simple (single line) single expression function",
        debt = Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val bodyExpression = function.bodyExpression ?: return

        val startLine = bodyExpression.node.firstChildNode.line(function.containingFile)
        val endLine = bodyExpression.node.lastChildNode.line(function.containingFile)
        val isMultiline = endLine != startLine

        val numberOfCalls = getCallsCount(bodyExpression)
        if (isMultiline) {
            report(createReport(function))
        } else if (numberOfCalls > MAX_CALL_CHAINS_IN_SINGLE_EXPRESSION) {
            report(createReport(function))
        }
    }

    private fun createReport(function: KtNamedFunction): Finding {
        return CodeSmell(
            issue = issue,
            entity = Entity.from(function),
            message = "Use only simple (single line) single expression function"
        )
    }

    private fun getCallsCount(expression: KtExpression): Int {
        return CallsCounterVisitor().let { visitor ->
            expression.accept(visitor)
            visitor.countOfChainCall
        }
    }

    private class CallsCounterVisitor : KtTreeVisitorVoid() {

        var countOfChainCall: Int = 0

        override fun visitCallExpression(expression: KtCallExpression) {
            super.visitCallExpression(expression)
            countOfChainCall++
        }
    }
}