# Github Package and Action
This repository will contain required source code and documentation regarding publishing java packages to github, automating the package publishing process and finally resolving published packages from there.

## bootstrapping java library with maven 
Let's bootstrap a java library with maven. we will use jdk 11, and junit5 for running the test. the changes are [here](https://github.com/kawnayeen/github-package-and-action/commit/af80d74bf2b638685933d797bd583062340758b5).

So, after doing this changes, if we run the following command from `java-lib-with-maven` directory,

```
mvn test
```

we will see the following output in console:-
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.kawnayeen.HelloLibTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.041 s - in com.kawnayeen.HelloLibTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.866 s
[INFO] Finished at: 2021-08-10T17:24:39+06:00
[INFO] ------------------------------------------------------------------------
```

## automating running test with github action

we can add our workflow files at `.github/workflows` directory. workflow file is basically a yml file which contains necessary steps and instructions to run our automation.

[Here](https://github.com/kawnayeen/github-package-and-action/commit/cbd650cdd988364b2250e2b6eb5b6e9b19d74325) is the workflow file to running test.

There is mainly two section of a workflow.
- First we need to define which event will trigger the workflow
- Then what we actually need to do 

For, example on this workflow file, the following basically telling at each `push` 
or `pull request` to `main` branch, we will run this workflow.

```
name: Running unit test
on:
   push:
     branches:
       - main
   pull_request:
     branches:
       - main
```

When the event trigger, we can run one or many jobs. By default, different jobs will run concurrently. Job for running test is following:-
```
jobs:
   unit_test:
     runs-on: ubuntu-latest
     defaults:
       run:
         working-directory: ./java-lib-with-maven
     permissions:
       contents: read
     steps:
       - uses: actions/checkout@v2
       - uses: actions/setup-java@v2
         with:
           java-version: '11'
           distribution: 'adopt'
       - name: Run unit test with maven
         run: mvn test
```
so, according to this configuration:-
- this job will run on `ubuntu-latest`
- it will run against the source code of `java-lib-with-maven` directory of the repository
- steps are:-
	- checkout the source code
	- setup java 11
	- run `mvn test`

we use `uses` to run another action from public repository or current repository.
for example, `uses: actions/checkout@v2` is running the source code checkout 
action from [here](https://github.com/actions/checkout).

## publishing packag to github

In order to publish package to github, we need to add the package repository in the 
pom.xml file. The changes is [here](https://github.com/kawnayeen/github-package-and-action/commit/d7f69c79cc7f6b65c101d99f0d0ad6dcefb9ddda).

now if we do `mvn deploy` from `java-lib-with-maven` directory, the package will be published to github. 

Now, in order to publish package at github, we need to authenticate. For that, setps are following:-
- Generate a personal access token (PAT). Follow [this](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token) documentation for this. make sure the token has package read and write permission.
- We need to update our `settings.xml` file at `.m2` directory. Follow [this](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-with-a-personal-access-token) documentation to do that.

## workflow for maven deploy

Workflow file for maven deploy is [here](https://github.com/kawnayeen/github-package-and-action/blob/main/.github/workflows/mavendeploy.yml).

This is similar to previous worklow file. We are providing `server-username` and `server-password` as `MVN_USER` and `MVN_PASS` respectively. so that workflow can publish this package to our specified repository. We need to add those value by 
following `Settings --> Secrets --> New repository secret`.

## resolving published packaged in maven project
Since we have published java package to github, now it's time to use that in other 
project. Let's go to the package page [here](https://github.com/kawnayeen/java-packages/packages/935857) and copy the dependency code.

now paste that in withing `dependencies` tag of `pom.xml`
```
<dependency>
  <groupId>com.kawnayeen</groupId>
  <artifactId>lib-maven</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

We also need to add the repository location, so that maven knows from where to 
resolve it.

```
<repositories>
  <repository>
    <id>github</id>
    <name>GitHub Apache Maven Packages</name>
    <url>https://maven.pkg.github.com/kawnayeen/java-packages</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

We can find a complete example of `pom.xml` [here](https://github.com/kawnayeen/github-package-and-action/blob/main/resolve-lib-with-maven/pom.xml).

As we already updated our `settings.xml` previously for publishing package, our existing settings.xml will be able to authenticate to the repository for downloading package.

## resolving published package from gradle project

### adding plugin to read from settings.xml
we need to use the following plugin to read configuration from settings.xml
```
id 'net.linguica.maven-settings' version '0.5'
```
in the plugins block
```
plugins {
  ...
  id 'net.linguica.maven-settings' version '0.5'
}
```

### adding github maven respository

in the `repositories` section, we need to add the following
```
maven {
    name "github"
    url "https://maven.pkg.github.com/kawnayeen/java-packages"
}
```

### finally adding our package
in the `dependencies` section
```
implementation 'com.kawnayeen:lib-maven:1.0-SNAPSHOT'
```

we can find a complete example of `build.gradle` file [here](https://github.com/kawnayeen/github-package-and-action/blob/main/resolve-lib-with-gradle/lib/build.gradle).

## resolving package in gradle build without settings.xml of .m2 directory
If we don't have access to `settings.xml` of `.m2` directory or we don't want to use that, we can still resolve packages by updating the `repository` followingly:-
```
maven {
    url "https://maven.pkg.github.com/kawnayeen/java-packages"
    credentials {
        username System.getenv('MVN_USER')
        password System.getenv('MVN_PASS')
    }
}
```
In that we can need to pass the username and password as environment variable.

## publishing library to gitub package with gradle
In order to publish package to github with gradle build system, we need to do the following:-
- adding `maven-publish` plugin. like following
```
plugins {
    ...
    id 'maven-publish'
    ...
}
```
- add publising block like following
```
publishing {
    publications {
        maven(MavenPublication) {
            from components.java
        }
    }
    repositories {
        maven {
            name = 'github'
            url = 'https://maven.pkg.github.com/kawnayeen/java-packages'
        }
    }
}
```

As we areadly using `net.linguica.maven-settings` plugin for reading `settings.xml` so only executing the following command will publish library

```
./gradlew publish
```

We can find the required changes in `build.gradle` file [here](https://github.com/kawnayeen/github-package-and-action/commit/99ac3122ffa23ad8ff30d447f0c3a22bed9f0291).
