import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

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
        val taskName = "generate${sourceSet.name.replaceFirstChar { it.uppercase() }}BuildConfig"
        val outputDir = project.layout.buildDirectory.dir("generated/buildConfig/${sourceSet.name}/kotlin")

        project.tasks.register(taskName, GenerateBuildConfigTask::class.java) {
            this.outputDir.set(outputDir)
            packageName.set(extension.packageName)
            objectName.set(extension.objectName)
            fields.set(extension.fields)
        }

        sourceSet.kotlin.srcDirs(outputDir)
    }
}