# FeatJAR-examples
This is the example project for [FeatJAR](https://github.com/FeatureIDE). It contains a collection of exemplary applications of the FeatJAR framework. 
Both the handling of the library directly and the use of the command line interface are described.

# Table of Contents
1. [Pre-Knowledge](#example)
2. [First Steps](#example2)
3. [Usage of Library direct ](#third-example)
4. [Usage with commandline ](#fourth-examplehttpwwwfourthexamplecom)

## Pre-Knowledge
This tutorial explains how to use the FeatJAR library. For this purpose, an overview of the intended application possibilities is first given. 

### Objects
- Feature Model
    - Propositional Formula
    - CNF
    - DDNNF
    - BDD
- Configuration
- Feature
- Implementation artifact

### Functions
#### Feature Model
- Load Feature Model
    - Parameters: Format, Preprocessing
- Store Feature Model
    - Parameters: Format
- Analyze Feature Model
    - Void
        - Parameters: partial configuration, temp constraints
    - Number of configurations
        - Parameters: partial configuration, temp constraints
    - Core/Dead
        - Parameters: feature selection, partial configuration, temp constraints
    - Atomic Sets
        - Parameters: feature selection, partial configuration, temp constraints
    - False Optional
        - Parameters: feature selection, partial configuration, temp constraints
    - Indeterminate Hidden
        - Parameters: feature selection, partial configuration, temp constraints
    - Redundant Constraints
        - Parameters: feature selection, partial configuration, temp constraints
    - Explanations
- Convert Feature Model
    - Propositional Formula
    - CNF
        - Parameters: transformation (distributive, tseytin, ...)
    - DDNNF
        - Parameters: properties
    - BDD
        - Parameters: properties
- Modify Feature Model
    - Add/Remove Features
    - Add/Remove constraints
    - Slice Feature Model
    - Project feature model
    - Decomposition
    - Composition
- Diff Feature Models

#### Configuration
- Load Configuration
- Store Configuration
- Generate Configurations
    - Manual
    - Random configuration
    - Update partial configuration (decision propagation)
    - Complete partial configuration
- Analyze Configurations
    - Valid
    - Complete
    - Selected/deselected ratio
    - Coverage

#### Samples
- Load Configuration List
- Store Configuration List
- Generate samples
    - Random
    - T-Wise
- Analyse Samples
    - Coverage
    - Selected/deselected ratio
    - Distribution

#### Implementation Artifacts
- Analyze implementation artifacts

## First Steps
### Requirements
+ IDE of your choice for example [Intellij](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/)
+ Version Control Systems [GIT](https://git-scm.com/)
+ Build-Tool [GRADLE](https://gradle.org/)
+ Java Version 14 

Look at [FeatJAR](https://github.com/FeatureIDE/FeatJAR) to see how to build the hole project.

## Usage of Library direct 
`a`

## Usage with commandline 
### count feature model solutions
`java -jar cli/build/libs/cli-*-all.jar --command countsharpsat --input cli/src/test/resources/testFeatureModels/car.xml`
`java -jar cli/build/libs/cli-*-all.jar --command isvoid --input cli/src/test/resources/testFeatureModels/car.xml`
`java -jar cli/build/libs/cli-*-all.jar --command coredead --input cli/src/test/resources/testFeatureModels/car.xml`



### or, equivalently, using Gradle
`./gradlew :cli:run --args " --command countsharpsat --input src/test/resources/testFeatureModels/car.xml"`