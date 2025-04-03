open class BuildConfigExtension {
    var packageName: String = "com.rcl"
    var objectName: String = "BuildConfig"
    val fields = mutableListOf<Field>()

    fun buildConfigField(type: String, name: String, value: String) {
        require(type.isNotBlank() && name.isNotBlank()) { "Type and name must not be blank" }
        fields.add(Field(type, name, value))
    }
}