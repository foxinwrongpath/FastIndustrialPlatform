# Mod Template Java

Mod Template for *Java* language modded environment with [**Magicbook Gradle**](https://plugins.gradle.org/plugin/io.gitlab.gregtechlite.magicbookgradle).

## How to Setup Workspace

- At first, confirm you set *JDK21* as your Gradle JVM version (**_exactly_**), this is used for **Magicbook Gradle** settings.
  If you already to set the Gradle JVM version, you can start loading Gradle scripts.
- When the Gradle scripts is loaded done (terminal info "`BUILD_SUCCESSFUL`" means it is completed), run the task `Setup Workspace`.
- Have a nice day with Minecraft development!

## Suggestions and Notices

- This template supported hot-swap with  `run Java17 Client` or `run Java 17 Server` task, this task used `LWJGL3ify` to let Minecraft 1.12.2 launched with Java 17.
  Hot-swap means the code changes will react in the launched game instantly (in development environment). These settings will not affect the packaged jar, it is
  only for development works.
- The option `enableModernJavaSyntax()` (in **Magicbook Gradle**) added Jabel and Java8Unsupported to `dependencies`, it will allow to use some modern syntax like
  var or record in Java 8 environment.
- We added JetBrains Annotation and Lombok in `dependencies` by default, if you do not use these dependencies, just delete it.