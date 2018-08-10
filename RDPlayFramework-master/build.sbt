name := """RDPlayFramework"""

scalaVersion := "2.12.6"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

routesGenerator := InjectedRoutesGenerator

libraryDependencies += guice
libraryDependencies += "com.google.inject" % "guice" % "3.0"
libraryDependencies += javaJpa
libraryDependencies += "com.typesafe.play" %% "play" % "2.4.0-M1"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.3.1.Final"

libraryDependencies += "org.apache.directory.studio" % "org.apache.commons.io" % "2.4"

libraryDependencies += "org.webjars" % "webjars-play" % "2.0"

libraryDependencies += javaWs % "test"
libraryDependencies += "junit" % "junit" % "4.12"
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % "test"
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % "test"
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java           // Java project. Don't expect Scala IDE
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)  // Use .class files instead of generated .scala files for views and routes