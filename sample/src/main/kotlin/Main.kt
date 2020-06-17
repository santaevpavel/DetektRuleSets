
@Suppress("MagicNumber", "MatchingDeclarationName")
class SampleClass {

    private val error = "NullPointerException"
    private val count = 5
    // private val property by lazy { 5 }
    private val constantGetterProperty
        get() = 5

    fun getAllErrors(): String {
        return buildString {
            repeat(5) {
                append(error)
            }
        }
    }

    fun complexSingleExpression() = buildString {
        append(5)
        reverse()
    }.toString()

    /*fun commentedFunction(): String {
        return buildString {
            repeat(5) {
                append(error)
            }
        }
    }*/
}
