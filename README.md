Kotlin/Native Builder
=====================

This application makes it simpler to update the
Kotlin/Native compiler, by building based off 
your Kotlin Java compiler version.

Prerequisites
-------------

You need to install Java Runtime Environment and
the Kotlin Java Runtime Compiler to run this program.

Install and run
---------------

#### Powershell

Install from GitHub:
```powershell
cd ~
git clone https://github.com/joshua-kent/native-build.git
```
Run program:
```powershell
cd native-build\out\artifacts\native_build_jar
java -jar native-build.jar
```

If you don't want the source code, you can now
run this to remove the repository that was just
installed (the program will be installed in
`C:\kotlin-native` and added to path anyway):
```$powershell
cd ~
ri native-build -recurse -force
```

How to use
----------
If you have already run the program using the
instructions above, it will now be copied into
`C:\kotlin-native` and added to path via a batch
file. This means that to run again, you can simply
use:
```powershell
native-build
```

In the future, functionality may be added to run
commands such as `native-build -update` to update
the utility itself.
