// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `hello-grpc-proto` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        library.grpcNetty,
        library.scalaPbRuntime,
        library.scalaPbRuntimeGrpc,
        library.scalaCheck % Test,
        library.scalaTest  % Test
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scalaCheck = "1.13.5"
      val scalaTest  = "3.0.3"
    }
    val grpcNetty          = "io.grpc"                 % "grpc-netty"           % com.trueaccord.scalapb.compiler.Version.grpcJavaVersion
    val scalaCheck         = "org.scalacheck"         %% "scalacheck"           % Version.scalaCheck
    val scalaPbRuntime     = "com.trueaccord.scalapb" %% "scalapb-runtime"      % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
    val scalaPbRuntimeGrpc = "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion
    val scalaTest          = "org.scalatest"          %% "scalatest"            % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  gitSettings ++
  headerSettings ++
  scalaPbSettings

lazy val commonSettings =
  Seq(
    // scalaVersion from .travis.yml via sbt-travisci
    // scalaVersion := "2.12.2",
    organization := "io.ontherocks",
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-Ywarn-unused-import"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    shellPrompt in ThisBuild := { state =>
      val project = Project.extract(state).currentRef.project
      s"[$project]> "
    }
)

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

import de.heikoseeberger.sbtheader.HeaderPattern
import de.heikoseeberger.sbtheader.license._
lazy val headerSettings =
  Seq(
    headers := Map("scala" -> Apache2_0("2017", "Petra Bierleutgeb"))
  )

lazy val scalaPbSettings = Seq(
  PB.targets in Compile := Seq(scalapb.gen() -> (sourceManaged in Compile).value)
)
