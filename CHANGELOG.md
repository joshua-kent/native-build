Changelog
===

Version 1.1 [BETA BUILD]
---

#### Released [DATE]

---

### Additions

- Clarity of whether in a development build,
beta build or stable release, with
warnings.
- Checks operating system for both internal decisions
and to check if the current operating system is
supported (raises an error if otherwise).
- Errors now use
`com.nativebuild.util.misc.NativeBuildException`.
- Now deletes a pre-existing zip file to avoid errors
if it already exists. (mainly for development)

### Changes

- License information is now displayed in the source
code of *native-build.bat*.
- References to 'non-official' have been replaced
with 'unofficial'.
- Renamed 'build' branch to 'beta', to not be confused
with development builds.
- Progress bar while deleting old installations now
properly measure the amount that has been deleted
rather than the amount that is still there.

#### `com.nativebuild.progressBar()`

- No longer loops and displays once.
- There is now a maximum bar length of 100 to lie
within the array size limit.

#### `com.nativebuild.util.misc.sizeOfDirectory()`

- Moved to
*src/com/nativebuild/util/misc/sizeOfDirectory* in
source code.

### Bug-fixes

- .zip file now successfully deletes.
- Progress bar should display properly in PowerShell.

#### `com.nativebuild.util.misc.sizeOfDirectory()`

- Added protection against errors when searching
through directories (such as null pointer errors.)

Version 1.0.3
---

#### Released 30 June 2020

---

This will **actually** be the last update until *v1.1*,
which will introduce Linux compatibility. Also, there
are a few changes to how the changelog is formatted.
The changelog will no longer be separated into 
functions, classes, etc. unless the addition is
directly to do with the source code. Rather, it will be
split into *'Changes'*, *'Additions'* and *'Bug-fixes'*,
with notes like this sometimes above. Furthermore, the
date of release will be displayed.

### Changes

- Changed the wording of the *'These steps usually
take a while, please wait.'* sentence to *'Many of
these steps usually take a while, please wait.'*

- Removed progress bar when deleting file as it does
not show any meaningful information.

- Small edits to *README.md* to clarify that this is
a non-official application and use better terminology.

#### `com.nativebuild.ProgressBar()`

- Shortened source code.

### Bug-fixes

- Minor bug fix to properly clear the output stream.

Version 1.0.2
---

- Downloading and extracting now use the new progress
bar feature detailed below.
- This will be the last update for a while until v1.1,
which is planned to introduce cross-compatibility.

### `com.nativebuild.progressBar()`

- Progress bars can now display exactly how much
something is done out of bytes, kilobytes, megabytes
or gigabytes (i.e. `10KB/148KB`).

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
