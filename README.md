# Overview

Java Dependency Validator is a Uber research tool aiming at detecting deep incompatibilities between third-party library dependency versions for a given project.

When upgrading dependency versions for a project, conflicts may arise due to removed or modified classes or methods in the new dependency version. When such classes or methods are being used directly from first party code, such conflicts are easy to detect and can be pointed out by the build/compilation itself. However, when the affected class or method is not directly used by the user code, but rather is used indirectly via another third-party library that remains unchanged, then the resulting issues are more difficult to detect and might introduce runtime bugs (see Java’s [NoSuchMethodError](https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/lang/NoSuchMethodError.html) and [ClassNotFoundException](https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/lang/ClassNotFoundException.html)). If the path under which the changed/removed class or method is used is not part of the common path or not covered by tests, these bugs can be surprisingly hard to guard against, or debug once they begin triggering in production.

Furthermore, just checking that all static references from every dependency A to each method of every other dependency B are correct for the given version of B can actually prove too restrictive. In practice, any given project uses only a subset of the APIs of each of its third-party dependencies, so we do not care about calls between A and B which are unreachable given our first-party project code.

Thus, Java Dependency Validator tries to find reachable calls to removed/changed methods and reachable load operations on removed/changed classes for an updated third-party dependency.

At a high level, the tool takes the full fat jar for the project (i.e. a jar which includes all classes for the service or application along with all third-party dependencies which might be needed to run the project atop the standard Java JDK; rarely, this might exclude certain runtimes like Spark, with the caveat that dependency validation won't be performed for such runtimes), along with all the individual pre-upgrade and post-upgrade dependency jars (e.g. the standard jars from Maven Central for the given dependency version before and after the change), and explicit knowledge of which classes and methods correspond to first-party code logic (root jar files and packages). It then performs reachability analysis to find missing classes and methods in the post-upgrade set which are reachable from the first-party code.


# Usage

## Preparing input

Assume that we are trying to determine potential third-party library dependency violations for a given repository commit point. In other words, we are trying to determine if an classes and methods (indirectly) used by the user code pre-commit (`HEAD~1`) are missing in the fat jar post-commit (`HEAD`).

Java Dependency validator takes five mandatory arguments

1. Four arguments including data obtained from the `HEAD` version of the repository
   - `fat.jar` - project's post-commit fat jar
   - `ext_jars.dat` - text file containing the paths to all third-party post-commit jar files
   - `root_jars.dat` - text file containing the paths to all first-party (service/app) post-commit root jar files
   - `root_packages.dat` - text file containing list of first-party (service/app) post-commit root packages (e.g. com.uber)
2. Two arguments including data obtained from the `HEAD~1` version of the repository
   - `pre_fat.jar` - project's pre-commit fat jar
   - `pre_ext_jars.dat` - text file containing the paths to all third-party pre-commit jar files
  
For each of the jar-related dat files, the format is simply one filesystem path per line pointing to each jar file to load/analyze. `root_packages.dat` is similar, but each line is a Java package name.

## Running the tool

It is best to give the tool as much memory as possible, so it is recommended to run the tool with generic java command. Run the tool with at least 10GB of Java heap (the order of arguments does not matter):

```bash
java -Xms12G -Xmx12G -jar dependency-validator.jar -fat_jar fat.jar -ext_jars ext_jars.dat -root_jars root_jars.dat -root_packages root_packages.dat -pre_fat_jar pre_fat.jar -pre_ext_jars pre_ext_jars.dat
```

The error report format is currently human-readable text and will look like the listing below

```bash
MISSING CLASSES FOUND:
        org/glassfish/jersey/internal/inject/InjectionManagerFactory
        org/glassfish/jersey/internal/inject/InjectionManager
MISSING CLASSES   2
```

If no missing classes or methods are reported, then the Java Dependency Validator considers the dependency upgrade safe (from the point of view of potential NoSuchMethodError/ClassNotFoundException issues).

## Citing this work

You are encouraged to cite the following paper if you use this tool in academic research:

```
(TO BE ADDED)
```

## Support

For research purposes only.  This project is released "as-is" with no support expected.  Updates may be posted, but are not guaranteed and no timeline is available.

## License

This project is copyright 2023 Uber Technologies, Inc., and licensed under Apache 2.0.
