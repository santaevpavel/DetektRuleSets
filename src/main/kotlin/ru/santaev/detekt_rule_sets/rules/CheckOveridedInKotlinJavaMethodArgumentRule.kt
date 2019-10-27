package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import org.jetbrains.kotlin.psi.psiUtil.isExtensionDeclaration
import ru.santaev.detekt_rule_sets.utils.isOveride

class CheckOveridedInKotlinJavaMethodArgumentRule : Rule() {

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "",
        debt = Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        val functionClass = function.containingClass() ?: return


        /*functionClass.
        function.isOveride()*/
    }

    /*private fun createReport(function: KtNamedFunction): Finding {
        return CodeSmell(
            issue = issue,
            entity = Entity.from(function),
            message = "Use usual member function instead of member extension function"
        )
    }*/
}