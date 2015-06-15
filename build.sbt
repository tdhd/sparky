val project = Project(
  id = "sparky-id",
  base = file("."),
  settings = Project.defaultSettings ++
             Seq(
               name := """sparky""",
               scalaVersion := "2.11.6", // "2.10.5", //"2.11.6",
               scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.7", "-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint"),
               javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m"),
               libraryDependencies ++= Seq(
                 "org.jsoup" % "jsoup" % "1.8.2",
                 "org.apache.spark" % "spark-core_2.11" % "1.4.0")
             )
)

