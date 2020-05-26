package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.psi.*
import ru.santaev.detekt_rule_sets.utils.KtElementParser
import ru.santaev.detekt_rule_sets.utils.log

class NoCommentedCodeRule(config: Config) : Rule(config) {

    private val kotlinParser = KtElementParser()
    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Do not keep commented code",
        debt = Debt.FIVE_MINS
    )

    override fun visitComment(comment: PsiComment) {
        super.visitComment(comment)
        if (shouldCommentBeDeleted(comment)) {
            report(createReport(comment))
        }
    }

    private fun createReport(comment: PsiComment): Finding {
        return CodeSmell(
            issue = issue,
            entity = Entity.from(comment),
            message = "Remove this comment or add TODO/FIXIT label to comment"
        )
    }

    private fun uncomment(comment: PsiComment): String {
        return if (comment.tokenType.index.toInt() == MULTILINE_COMMENT_TOKE_TYPE_CODE) {
            comment.text
                .substringAfter(MULTILINE_COMMENT_START)
                .substringBeforeLast(MULTILINE_COMMENT_END)
        } else {
            comment.text.substringAfter(SINGLE_LINE_COMMENT_START)
        }
    }

    private fun shouldCommentBeDeleted(comment: PsiComment): Boolean {
        val ktFile = kotlinParser.parseString(uncomment(comment))
        val ktElements = KtElementCountCalculatorVisitor().let { visitor ->
            visitor.visitKtElement(ktFile)
            visitor.ktElements
        }
        val codeFactor = ktElements.toDouble() / ktFile.text.split(" ", "\n").size
        log("codeFactor $ktElements $codeFactor")

        return codeFactor > CODE_FACTOR_THRESHOLD && !isAllowedByWhitelistedWords(comment)
    }

    private fun isAllowedByWhitelistedWords(comment: PsiComment): Boolean {
        val allowWhitelistedWords = ruleSetConfig.valueOrDefault(ALLOW_TODO_FIX_IT_CONFIG_KEY, false)
        return allowWhitelistedWords && WHITELISTED_WORDS.any { comment.text.contains(it, ignoreCase = true) }
    }

    companion object {
        private const val MULTILINE_COMMENT_START = "/*"
        private const val MULTILINE_COMMENT_END = "*/"
        private const val SINGLE_LINE_COMMENT_START = "//"
        private const val CODE_FACTOR_THRESHOLD = 0.05
        private const val MULTILINE_COMMENT_TOKE_TYPE_CODE = 12
        private const val ALLOW_TODO_FIX_IT_CONFIG_KEY = "allowToDoAndFixIt"
        private val WHITELISTED_WORDS = listOf("todo", "fixit")
    }
}

private class KtElementCountCalculatorVisitor : KtTreeVisitorVoid() {

    var ktElements = 0

    override fun visitKtElement(element: KtElement) {
        super.visitKtElement(element)
        if (!IGNORE_LIST.contains(element::class)) {
            log("visit $element ${element::class.simpleName} ${element.text}")
            ktElements++
        }
    }

    companion object {
        private val IGNORE_LIST = listOf(
            KtPackageDirective::class,
            KtImportList::class,
            KtReferenceExpression::class,
            KtBlockExpression::class,
            KtBinaryExpression::class,
            KtNameReferenceExpression::class,
            KtOperationReferenceExpression::class
        )
    }
}