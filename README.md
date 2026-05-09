# DD-7dRL2018
Dragon Dive, a 7 Day Roguelike for 2018 about riding dragons and then falling off of them.

The dragon is inplied, the falling is real!

## Building

From [Code/](Code), run:

```bash
./gradlew :desktop:distJar
```

This produces a runnable fat jar in `Code/desktop/build/libs/`.

## Local Executable Build (CLI)

From [Code/](Code), run:

```bash
./gradlew :desktop:localExecutableZip
```

This will:

- Build the fat jar
- Run `jpackage` for your current OS
- Create a zip in `Code/desktop/build/release/`

If you only want the unpacked app-image (no zip), run:

```bash
./gradlew :desktop:localExecutable
```

## Automated Releases

Every push to `main` triggers the workflow in `.github/workflows/release-on-main.yml`.
Each run:

- Builds the desktop app on Linux, Windows, and macOS
- Packages a native app image for each platform
- Creates a new GitHub Release tagged with a unique version derived from git history
- Uploads platform zip files to the release
