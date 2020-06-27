Changelog
===

Version 1.0.0rc5 (IN-PROGRESS)
---

### `com.nativebuild.util.Menu` class


- Added `com.nativebuild.util.Menu` class. This is
intended to give standardised menu functionality
that is mainstreamed across the entire project.

---

### `com.nativebuild.util.Build` class

- Added documenation for `Build` class.

---

### `com.nativebuild.util.License` class

- Created class and its main function,
`displayLicense()`, which displays the license
and waits for user input to then return to the
`main()` function.

---

### `com.nativebuild.util.Info` class

- coming soon

--

### `main` function

- Added 'info' menu option.
- Added 'license' menu option.
- Changed menus into `com.nativebuild.util.Build`
objects.
- Moved to package `com.nativebuild`

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