# Overview

This tool attempts to help with problems that can arise from introducing diffs that also include modifications of the third-party library dependencies (changing/adding/removing a given version of a third-party library). This is particularly dangerous and difficult to detect if an affected library is not directly used by the user code (as then potential incompatibilities would be caught at build time) but rather is used indirectly via another third-party library that remains unchanged. The tool currently focuses on detecting missing class and missing method errors that may be a result of such third-party library dependency. At a high level, it takes a list of classes and methods defined in root jar files (i.e., jar files that define the user code logic), as well as a list of classes and methods missing in the application code after third-party library dependency change, and performs reachability analysis to find missing classes and methods reachable from from the roots.

# Usage

## Preparing input

Assume that we are trying to determine potential third-party library dependency violations for a given repository commit point. In other words, we are trying to determine if an classes and methods (indirectly) used by the user code pre-commit (`HEAD~1`) are missing in the fat jar post-commit (`HEAD`).

Dependency validator takes five mandatory arguments

1. Four arguments including data obtained from the `HEAD` version of the repository
   - `fat.jar` - service's post-commit fat jar
   - `ext_jars.dat` - text file containing list of third-party post-commit jar files
   - `root_jars.dat` - text file containing list of service's post-commit root jar files
   - `root_packages.dat` - text file containing list of service's post-commit root packages
2. Two arguments including data obtained from the `HEAD~1` version of the repository
   - `pre_fat.jar` - service's pre-commit fat jar
   - `pre_ext_jars.dat` - text file containing list of third-party pre-commit jar files

## Running the tool

It is best to give the tool as much memory as possible, so it is recommended to run the tool with generic java command. Run the tool with at least 10GB of Java heap (the order of arguments does not matter):

```bash
java -Xms12G -Xmx12G -jar dependency-validator.jar -fat_jar fat.jar -ext_jars ext_jars.dat -root_jars root_jars.dat -root_packages root_packages.dat -pre_fat_jar pre_fat.jar -pre_ext_jars pre_ext_jars.dat
```

The error report format will look like the below

```bash
MISSING CLASSES FOUND:
        org/glassfish/jersey/internal/inject/InjectionManagerFactory
        org/glassfish/jersey/internal/inject/InjectionManager
MISSING CLASSES   2
```

## Citing this work

You are encouraged to cite the following paper if you use this tool in academic research:

```
(TO BE ADDED)
```

## Support

For research purposes only.  This project is released "as-is" with no support expected.  Updates may be posted, but are not guaranteed and no timeline is available.

## License

This project is copyright 2023 Uber Technologies, Inc., and licensed under Apache 2.0.
