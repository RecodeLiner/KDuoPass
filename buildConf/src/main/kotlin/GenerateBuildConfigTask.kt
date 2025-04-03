import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateBuildConfigTask : DefaultTask() {

    @OutputDirectory
    val outputDir = project.objects.directoryProperty()

    @Input
    val packageName = project.objects.property(String::class.java)

    @Input
    val objectName = project.objects.property(String::class.java)

    @Input
    val fields = project.objects.listProperty(Field::class.java)

    @TaskAction
    fun generate() {
        val dir = prepareOutputDir()
        val file = File(dir, "${objectName.get()}.kt")
        file.writeText(generateFileContent())
    }

    private fun prepareOutputDir(): File {
        return outputDir.get().asFile.apply {
            if (exists()) deleteRecursively()
            mkdirs()
        }
    }

    private fun generateFileContent(): String {
        return buildString {
            appendLine("package ${packageName.get()}")
            appendLine()
            appendLine("object ${objectName.get()} {")
            fields.get().forEach { field ->
                appendLine("    const val ${field.name}: ${field.type} = ${field.value.formatValue(field.type == "String")}")
            }
            appendLine("}")
        }
    }

    private fun String.formatValue(isString: Boolean): String =
        if (isString) "\"$this\"" else this
}