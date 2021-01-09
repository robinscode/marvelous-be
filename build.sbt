lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := "marvelousbe",
    version := "2.8.x",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      javaJpa,
      "org.projectlombok" % "lombok" % "1.18.16",
      "com.h2database" % "h2" % "1.4.199",
      "org.hibernate" % "hibernate-core" % "5.4.9.Final",
      "org.awaitility" % "awaitility" % "4.0.1" % "test",
      "org.assertj" % "assertj-core" % "3.14.0" % "test",
      "org.mockito" % "mockito-core" % "3.1.0" % "test",
    ),
    PlayKeys.externalizeResources := false,
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v")),
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    )
  )

