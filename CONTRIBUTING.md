# Contributing to BetterF3
[![Crowdin](https://badges.crowdin.net/betterf3/localized.svg)](https://crowdin.com/project/betterf3) [![License: MIT](https://img.shields.io/badge/License-MIT-2230f2.svg)](https://github.com/TreyRuffy/BetterF3/blob/1.17.1-Forge/BetterF3-License)

If you would like to improve BetterF3, there are several ways you can help.
* You can make a financial donation by checking the "Sponsor this project" sidebar on the root GitHub page.
* You can contribute to the language files by either adding a PR or checking out our [Crowdin Page](https://crowdin.com/project/betterf3).
* If you are a developer, check below!

## Developers

### Requirements
In order to effectively contribute to our project, you need to follow these requirements:
* You need `git` installed. You can download it from [here](https://gitforwindows.org/) on Windows.
* You need Java 16 or later JDK. Check out [Adoptium](https://adoptium.net/) for most platforms.
* You need to use either [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Visual Studio Code](https://code.visualstudio.com/). Other IDEs are unsupported and may or may not work.

### Compiling
You can compile the source code here:
1. Clone this project to your local machine
2. This project is split into three parts.
    1. `common` holds all the common files between Forge and Fabric
    2. `fabric` holds all the files for only Fabric
    3. `forge` holds all the files for only Forge
3. Type `./gradlew build` in a terminal to build the project. On Windows, leave out `./` at the beginning for all
   `gradlew` commands if nothing works.
4. `cd` into either the `fabric` or `forge` directory, then `cd` to `build/libs` and the compiled mod will be in the
   format `betterf3-{version}-{modloader}.jar` (subject to change).

### Pull Requests
All pull requests need to comply with our CheckStyle requirements.
* Run `./gradlew checkstyleMain` in order to check if you comply.
* (Recommended) import the `.checkstyle/checkstyle.xml` file into your IDE.

### Creating Modules
In order to create a new module, either in this mod or your own, follow the directions in [CreateModule.md](docs/developers/CreateModule.md)