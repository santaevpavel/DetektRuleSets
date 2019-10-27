package ru.santaev.detekt_rule_sets.provider

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import ru.santaev.detekt_rule_sets.rules.LambdaImplicitParameterUsingRule
import ru.santaev.detekt_rule_sets.rules.NoMemberExtensionRule

class DetektRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "SantaevRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                NoMemberExtensionRule(),
                LambdaImplicitParameterUsingRule()
            )
        )
    }
}