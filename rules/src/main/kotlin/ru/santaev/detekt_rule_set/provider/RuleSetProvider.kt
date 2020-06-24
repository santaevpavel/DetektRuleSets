package ru.santaev.detekt_rule_set.provider

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import ru.santaev.detekt_rule_set.rules.ComplexSingleExpressionFunctionRule
import ru.santaev.detekt_rule_set.rules.LambdaImplicitParameterUsingRule
import ru.santaev.detekt_rule_set.rules.NoCommentedCodeRule
import ru.santaev.detekt_rule_set.rules.NoMemberExtensionRule

class DetektRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "extrarules"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            id = ruleSetId,
            rules = listOf(
                NoMemberExtensionRule(),
                LambdaImplicitParameterUsingRule(),
                ComplexSingleExpressionFunctionRule(),
                NoCommentedCodeRule(config)
            )
        )
    }
}