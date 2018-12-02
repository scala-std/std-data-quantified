val testLibraries = List(
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
  "org.typelevel" %% "discipline" % "0.10.0" % "test",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test")

val catsLibraries = List(
  "org.typelevel" %% "cats-core" % "1.5.0-RC1")

lazy val commonSettings = List(
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"),
  organization      := "com.alexknvl",
  version           := "0.4.0",
  scalaVersion      := "2.12.1",
  scalaOrganization := "org.typelevel",
  crossScalaVersions := Seq("2.11.8"),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  scalacOptions ++= List(
    "-deprecation", "-unchecked", "-feature",
    "-encoding", "UTF-8",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-Yno-adapted-args", "-Ywarn-dead-code",
    "-Yliteral-types",
    "-Ywarn-numeric-widen", "-Xfuture"),
  resolvers ++= List(
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases")),
  libraryDependencies ++= testLibraries,
  wartremoverWarnings ++= Warts.all
)

lazy val root = (project in file("."))
  .settings(name := "polymorphic")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= catsLibraries)
