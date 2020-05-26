package ru.santaev.detekt_rule_sets.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.psi.*
import ru.santaev.detekt_rule_sets.utils.KtFileParser
import ru.santaev.detekt_rule_sets.utils.line

class NoCommentedCodeRule : Rule() {

    private val kotlinParser = KtFileParser()
    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.CodeSmell,
        description = "Do not keep commented code",
        debt = Debt.FIVE_MINS
    )

    override fun visitComment(comment: PsiComment) {
        super.visitComment(comment)
        val ktFile = kotlinParser.parseString(uncomment(comment))
        val visitor = KtElementCountCalculatorVisitor()
        visitor.visitKtFile(ktFile)
        val codeFactor = visitor.ktElements.toDouble() / ktFile.text.split(" ", "\n").size
        println("codeFactor ${visitor.ktElements} $codeFactor")

        if (codeFactor > CODE_FACTOR_THRESHOLD) {
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

    companion object {
        private const val MULTILINE_COMMENT_START = "/*"
        private const val MULTILINE_COMMENT_END = "*/"
        private const val SINGLE_LINE_COMMENT_START = "//"
        private const val CODE_FACTOR_THRESHOLD = 0.01
        private const val MULTILINE_COMMENT_TOKE_TYPE_CODE = 12
    }
}

private class KtElementCountCalculatorVisitor : KtTreeVisitorVoid() {

    var ktElements = 0

    override fun visitKtElement(element: KtElement) {
        super.visitKtElement(element)
        if (element !is KtPackageDirective && element !is KtImportList) {
            ktElements++
        }
    }
}