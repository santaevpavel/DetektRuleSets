package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.isExtensionDeclaration

class NoMemberExtensionRule : Rule() {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Use usual member function instead of member extension function",
        debt = Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val isExtension = function.isExtensionDeclaration()
        val isMember = !function.isTopLevel

        if (isExtension && isMember) {
            report(createReport(function))
        }
    }

    private fun createReport(function: KtNamedFunction): Finding {
        return CodeSmell(
            issue = issue,
            entity = Entity.from(function),
            message = "Use usual member function instead of member extension function"
        )
    }
}