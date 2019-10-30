package ru.santaev.detekt_rule_sets.utils

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.com.intellij.psi.PsiFile
import org.jetbrains.kotlin.diagnostics.DiagnosticUtils
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction


fun KtNamedFunction.isOveride(): Boolean {
    return modifierList?.hasModifier(KtTokens.OVERRIDE_KEYWORD) == true
}

fun ASTNode.line(inFile: PsiFile): Int {
    return DiagnosticUtils.getLineAndColumnInPsiFile(inFile, this.textRange).line
}