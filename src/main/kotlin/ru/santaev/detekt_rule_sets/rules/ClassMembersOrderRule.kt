package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*

class ClassMembersOrderRule(config: Config) : Rule(config) {

    companion object {
        private const val KEY_MAIN = "class-members-order"

        private const val MEMBER_COMPANION_OBJECT = "CompanionObject"
        private const val MEMBER_PUBLIC_PROPERTY = "PublicProperty"
        private const val MEMBER_PROTECTED_PROPERTY = "ProtectedProperty"
        private const val MEMBER_PRIVATE_PROPERTY = "PrivateProperty"
        private const val MEMBER_CONSTRUCTOR = "Constructor"
        private const val MEMBER_PUBLIC_CONSTRUCTOR = "PublicConstructor"
        private const val MEMBER_PROTECTED_CONSTRUCTOR = "PublicConstructor"
        private const val MEMBER_PRIVATE_CONSTRUCTOR = "PrivateConstructor"
        private const val MEMBER_FUNCTION = "Function"
        private const val MEMBER_PUBLIC_FUNCTION = "PublicFunction"
        private const val MEMBER_PROTECTED_FUNCTION = "ProtectedFunction"
        private const val MEMBER_PRIVATE_FUNCTION = "PrivateFunction"

        private val DEFAULT_PRIORITIES = mapOf(
            MEMBER_COMPANION_OBJECT to 1,
            MEMBER_PUBLIC_PROPERTY to 2,
            MEMBER_PROTECTED_PROPERTY to 3,
            MEMBER_CONSTRUCTOR to 4,
            MEMBER_FUNCTION to 5
        )
    }

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Follow class members order",
        debt = Debt.FIVE_MINS
    )

    private val memberPriorities: Map<String, Int> = DEFAULT_PRIORITIES

    override fun visitClassBody(classBody: KtClassBody) {
        super.visitClassBody(classBody)
        classBody.acceptChildren(ClassMembersVisitor())
    }

    private fun createReport(function: KtNamedFunction): Finding {
        return CodeSmell(
            issue = issue,
            entity = Entity.from(function),
            message = "Use usual member function instead of member extension function"
        )
    }

    private class ClassMembersVisitor: KtVisitorVoid() {

        override fun visitKtElement(element: KtElement) {
            super.visitKtElement(element)
            if (element.parent is KtClassBody) {
                println(element.name)
            }
        }
    }
}