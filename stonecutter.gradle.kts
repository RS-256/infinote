plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.1.0" apply false
}

stonecutter active "26.1.2"


// Make newer versions be published last
stonecutter tasks {
    order("publishModrinth")
//    order("publishCurseforge")
}

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = property("mod.id") != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String
}

tasks.register("runClientCurrentVersion") {
    group = "run"
    description = "Runs :<current>:runClient only."
    dependsOn(project(":${sc.current?.version}").tasks.named("runClient"))
}

tasks.register("runServerCurrentVersion") {
    group = "run"
    description = "Runs :<current>:runServer only."
    dependsOn(project(":${sc.current?.version}").tasks.named("runServer"))
}

val releaseVersions = listOf(
    "1.17.1",
    "1.18.2",
    "1.19.2",
    "1.19.4",
    "1.20.2",
    "1.20.6",
    "1.21.4",
    "1.21.5",
    "1.21.9",
    "1.21.11",
    "26.1.2"
)

tasks.register("buildReleaseRemapped") {
    group = "build"
    description = "Build remapped jars only for release representative versions."
    dependsOn(releaseVersions.map { v -> ":$v:buildAndCollectRemapped" })
}

tasks.register("publishAllToModrinthRelease") {
    group = "publishing"
    description = "Publish all release versions in order"

    dependsOn(
        releaseVersions.map { ":$it:publishModrinth" }
    )
}

gradle.projectsEvaluated {
    releaseVersions.zipWithNext().forEach { (prev, next) ->
        project(":$next").tasks.named("publishModrinth") {
            mustRunAfter(":$prev:publishModrinth")
        }
    }
}