# SimpleDB-Kotlin

![status-wip](https://img.shields.io/badge/status-WIP-blue.svg)

A modernized codebase of SimpleDB in Kotlin with Gradle, JUnit, etc..

Currently based on [SimpleDB-2.10](http://www.cs.bc.edu/~sciore/simpledb/intro.html). The original author is Edward Sciore (sciore@bc.edu).

This project is still in WIP state. To-dos include:
- [ ] Migrate `StudentClient` into tests
    - [x] `createTable`
    - [x] `Findmajors`
    - [x] `ChangeMajor`
    - [ ] add new tests
- [ ] Fix nullability & synchronization issues.
- [ ] Introduce continous integration

## Usage

Read [Original README](https://github.com/htfy96/simpledb-kotlin/tree/master/README-OLD.txt) first!

### Run server
```bash
## Linux
./gradlew run

## Windows
./gradlew.bat run
```

The server will create database under `studentdb/` directory. Built sources could be found at `build/distributions`

### Generate docs
Doc system has been migrated to Dokka. Run
```bash
## Linux
./gradlew dokka

## Windows
./gradlew.bat dokka
```

to generate docs at `build/javadoc`.

### Generate IDEA project

**`gradle idea` has been deprecated**

The recommended way to load this project in IDEA is to click Open - select this directory - Click OK.

After that, IDEA will prompt you to `Import Gradle project`. Click that link, run with default configuration.
Now this project is opened in IDEA.

To run tests in IDEA, right click `src/test`, choose `Run All tests`.

To launch the server, right click `src/main/kotlin/simpledb/server/Startup`, choose `Run 'simpledb.server.Startup'`.

### Run tests
```bash
## Linux
./gradlew test

## Windows
./gradlew.bat test
```

If anything proceeds without error, you should see:
```
> Task :junitPlatformTest
new transaction: 1
recovering existing database
...
Test run finished after 8254 ms
[         2 containers found      ]
[         0 containers skipped    ]
[         2 containers started    ]
[         0 containers aborted    ]
[         2 containers successful ]
[         0 containers failed     ]
[         1 tests found           ]
[         0 tests skipped         ]
[         1 tests started         ]
[         0 tests aborted         ]
[         1 tests successful      ]
[         0 tests failed          ]
```
