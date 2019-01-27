import microsites._
import ReleaseTransformations._
import sbtcrossproject.{crossProject, CrossType}

inThisBuild(
  List(
    organization := "org.pure4s",
    sonatypeProfileName := "org.pure4s",
    homepage := Some(url("https://github.com/pure4s/logger4s")),
    licenses := List(
      "Apache-2.0" -> url("https://opensource.org/licenses/MIT")),
    developers := List(
      Developer(
        "llfrometa89",
        "Liván Frómeta",
        "llfrometa@gmail.com",
        url("http://pure4s.org/logger4s/")
      )
    )
  ))

lazy val V = new {
  val catsVersion = "1.5.0"
  val catsEffectVersion = "1.1.0"
  val scalaTestVersion = "3.0.5"
  val macroParadiseVersion = "2.1.1"
  val kindProjectorVersion = "0.9.9"
  val loggingScalaVersion = "3.5.0"
  val logbackClassicVersion = "1.2.3"
}

val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false,
  skip in publish := true
)

val buildSettings = Seq(
  organization := "org.pure4s",
  scalaVersion := "2.12.8",
  licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
  crossScalaVersions := Seq("2.11.12", scalaVersion.value),
  scalacOptions in (Compile, console) ~= filterConsoleScalacOptions,
  scalacOptions in (Compile, doc) ~= filterConsoleScalacOptions,
  scalafmtOnCompile in ThisBuild := true
)

val commonDependencies = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % V.catsVersion,
    "org.typelevel" %% "cats-effect" % V.catsEffectVersion,
    "org.scalatest" %% "scalatest" % V.scalaTestVersion % Test
  )
)

val compilerPlugins = Seq(
  libraryDependencies ++= Seq(
    compilerPlugin(
      "org.scalamacros" %% "paradise" % V.macroParadiseVersion cross CrossVersion.full),
    compilerPlugin(
      "org.spire-math" %% "kind-projector" % V.kindProjectorVersion)
  )
)

lazy val logger4s = project
  .in(file("."))
  .settings(buildSettings)
  .settings(noPublishSettings)
  .dependsOn(coreJVM)
  .aggregate(coreJVM)

lazy val core = crossProject(JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(moduleName := "logger4s")
  .settings(buildSettings)
  .settings(commonDependencies)
  .settings(compilerPlugins)
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % V.loggingScalaVersion,
      "ch.qos.logback" % "logback-classic" % V.logbackClassicVersion
    ))

lazy val coreJVM = core.jvm

lazy val example = project
  .in(file("example"))
  .settings(buildSettings)
  .settings(noPublishSettings)
  .settings(compilerPlugins)
  .dependsOn(coreJVM)

addCommandAlias(
  "validateScalafmt",
  ";sbt:scalafmt::test;test:scalafmt::test;compile:scalafmt::test")
