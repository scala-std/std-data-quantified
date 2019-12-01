val testLibraries = List(
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
  "org.typelevel" %% "discipline-core" % "1.0.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test")

val catsLibraries = List(
  "org.typelevel" %% "cats-core" % "2.0.0")

lazy val commonSettings = List(
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
  organization      := "com.alexknvl",
  version           := "0.5.0",
  scalaVersion      := "2.12.0",
  scalaOrganization := {
    if (CrossVersion.partialVersion(scalaVersion.value).exists(_._2 >= 13))
      "org.scala-lang"
    else
      "org.typelevel"
  },
  crossScalaVersions := Seq("2.13.0"),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  scalacOptions ++= List(
    "-deprecation", "-unchecked", "-feature",
    "-encoding", "UTF-8",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions") ++ {
    if (CrossVersion.partialVersion(scalaVersion.value).exists(_._2 < 13))
      List("-Yno-adapted-args", "-Ywarn-dead-code", "-Yliteral-types", "-Ywarn-numeric-widen", "-Xfuture")
    else Nil
  },
  resolvers ++= List(
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases")),
  libraryDependencies ++= testLibraries
)

lazy val root = (project in file("."))
  .settings(name := "polymorphic")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= catsLibraries)
