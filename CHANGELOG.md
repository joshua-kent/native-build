Changelog
===

Version 1.0.0rc5.1
---

- Removed '(IN-PROGRESS)' from '1.0.0rc5' in
changelog.


Version 1.0.0rc5
---

### `com.nativebuild.util.Menu` class


- Added `com.nativebuild.util.Menu` class. This is
intended to give standardised menu functionality
that is mainstreamed across the entire project.

---

### `com.nativebuild.util.displayLicense()`

- Created `displayLicense()`, which displays the
license and waits for user input to then return to
the `main()` function.

---

### `com.nativebuild.util.displayInfo()`

- Created `displayInfo()`, which displays
information about this utility and waits for user
input to return to the `main()` function.

---

### `com.nativebuild.main()`

- Added 'info' menu option.
- Added 'license' menu option.
- Changed menus into `com.nativebuild.util.Build`
objects.
- Moved to package `com.nativebuild`
- Menu when a new version is available has been
changed to directly ask the question before the
options.

---

### Miscellaneous

- Added CHANGELOG.md (this!) to document changes
between versions in a more readable format than
comparing commits.
- Added 'build' repository to host in-progress
versions.
- Most files that were in the package
`com.nativebuild` are now in
`com.nativebuild.util`.
- Added short license into files as a comment.
- Full version info is now displayed as
`[version] at [day month year] [time] [UTC+n]`,
rather than `[version] [time] [day month year]`
- Renamed repository to 'native-build'.

### Bugfixes

- Fixed bug where you could possibly nest
`main()` functions, which would cause the
program to continue even after you ran the
utility.