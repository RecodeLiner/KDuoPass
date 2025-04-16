import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

@Suppress("unused")
class BuildConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("buildConfig", BuildConfigExtension::class.java)

        if (target.extensions.findByType(KotlinMultiplatformExtension::class.java) != null) {
            target.extensions.configure(KotlinMultiplatformExtension::class.java) {
                sourceSets.forEach { sourceSet ->
                    if (sourceSet is KotlinSourceSet) {
                        configureSourceSet(target, sourceSet, extension)
                    }
                }
            }
        } else {
            target.extensions.configure(org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension::class.java) {
                sourceSets.forEach { sourceSet ->
                    if (sourceSet is KotlinSourceSet) {
                        configureSourceSet(target, sourceSet, extension)
                    }
                }
            }
        }
    }

    private fun configureSourceSet(project: Project, sourceSet: KotlinSourceSet, extension: BuildConfigExtension) {
        val sourceSetNameCapitalized = sourceSet.name.replaceFirstChar { it.uppercaseChar() }
        val taskName = "generate${sourceSetNameCapitalized}BuildConfig"
        val outputDir = project.layout.buildDirectory.dir("generated/buildConfig/${sourceSet.name}/kotlin")

        sourceSet.kotlin.srcDir(outputDir)

        val generateTaskProvider = project.tasks.register(taskName, GenerateBuildConfigTask::class.java) {
            this.outputDir.set(outputDir)
            this.packageName.set(extension.packageName)
            this.objectName.set(extension.objectName)
            this.fields.set(extension.fields)
        }

        project.afterEvaluate {
            val dir = outputDir.get().asFile
            val stubFile = File(dir, "${extension.objectName}.kt")
            if (!stubFile.exists()) {
                dir.mkdirs()
                stubFile.writeText(
                    """
            |// GENERATED STUB
            |package ${extension.packageName}
            |
            |/** Temporary stub, will be overwritten during build **/
            |object ${extension.objectName} {}
            """.trimMargin()
                )
            }
        }

        val compileTaskName = "compileKotlin$sourceSetNameCapitalized"
        project.tasks.matching { it.name == compileTaskName }.configureEach {
            dependsOn(generateTaskProvider)
        }

        // Привязываем к build
        project.tasks.named("build").configure {
            dependsOn(generateTaskProvider)
        }

        // Привязываем к Gradle sync через prepareKotlinIdeaImport
        project.tasks.matching { it.name == "prepareKotlinIdeaImport" }.configureEach {
            dependsOn(generateTaskProvider)
        }
    }
}