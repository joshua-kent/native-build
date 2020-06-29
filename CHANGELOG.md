Changelog
===

Version 1.0.1
---

- Fixed minor bug that caused duplicated semicolons in
path after removing previous releases from path.

Version 1.0.0
---

- Entering invalid input now asks to try again
instead of exiting the utility.
- Downloads and extractions that take time now use
a progress bar to display how much they have progressed.

### `com.nativebuild.startMenu()`

- New function to handle the start menu, rather
than being included inside of
`com.nativebuild.main()`.
- Added documentation.

### `com.nativebuild.main()`

- Added documentation.

### `com.nativebuild.progressBar()`

- Creates a progress bar.

Version 1.0.0rc5.2
---

- Added documentation in source code.
- Removed initialiser from `com.nativebuild.Menu`
class to avoid non-intuitive behaviour.

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