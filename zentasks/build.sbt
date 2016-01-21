name := "ZenTasks"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  javaEbean
)     

play.Project.playScalaSettings
