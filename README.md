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
