organization := Settings.Organization
name         := "std-data-quantified"
version      := Settings.Version
licenses     += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= {
  if (Settings.standalone.value) Nil
  else List[ModuleID]()
}

libraryDependencies ++= List(
  Dependencies.scalacheck % Test ,
  Dependencies.scalaTest  % Test,

  Dependencies.macroCompat,
  scalaOrganization.value % "scala-compiler" % scalaVersion.value % Provided
)

addCompilerPlugin(Dependencies.Plugin.kindProjector)

Settings.standalone := true
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
publishM2Configuration    := publishM2Configuration.value.withOverwrite(true)

scalaVersion       := "2.12.4-bin-typelevel-4"
crossScalaVersions := Seq("2.13.0")

scalaOrganization := {
  if (CrossVersion.partialVersion(scalaVersion.value).exists(_._2 >= 13))
    "org.scala-lang"
  else
    "org.typelevel"
}

scalacOptions ++= List(
  "-deprecation", "-unchecked", "-feature",
  "-encoding", "UTF-8",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros") ++ {
  if (CrossVersion.partialVersion(scalaVersion.value).exists(_._2 < 13))
    List("-Yno-adapted-args", "-Ywarn-dead-code", "-Yliteral-types",
      "-Ywarn-numeric-widen", "-Xfuture")
  else Nil
}