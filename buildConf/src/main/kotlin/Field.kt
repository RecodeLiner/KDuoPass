import java.io.Serializable

data class Field(
    val type: String,
    val name: String,
    val value: String
) : Serializable