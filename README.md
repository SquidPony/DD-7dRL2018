# Dragon Dive
Dragon Dive, a 7-Day Roguelike for 2018 about riding dragons and then falling off of them.

The dragon is implied, the falling is real!

## Project Structure

- `assets`: shared assets used by all platforms.
- `core`: shared game logic and data.
- `lwjgl3`: desktop launcher and platform config.
- `teavm`: browser launcher and platform config.
- `gradlew`: Gradle wrapper entrypoint for all scripts.

## How to Play (Run Locally)

From this folder, run:

```bash
./gradlew lwjgl3:run
```

This starts the desktop game using assets from `assets/`.

## Building and Packaging

From this folder, run:

```bash
./gradlew lwjgl3:jar
```

This produces a runnable fat jar in `lwjgl3/build/libs/`.

## Local Executable Build (CLI)

From this folder, run:

```bash
./gradlew lwjgl3:packageWinX64
./gradlew lwjgl3:packageLinuxX64
./gradlew lwjgl3:packageMacM1
./gradlew lwjgl3:packageMacX64
```

This will:

- Build the fat jar
- Download 4 different OpenJDK 21 releases for different OSes
- Create 4 zip files in `lwjgl3/build/construo/dist/`

If you only want the unpacked app-image (no zip), run:

```bash
./gradlew lwjgl3:roastWinX64
./gradlew lwjgl3:roastLinuxX64
./gradlew lwjgl3:roastMacM1
./gradlew lwjgl3:roastMacX64
```

This will create platform-specific executables in
`lwjgl3/build/construo/winX64/roast`,
`lwjgl3/build/construo/linuxX64/roast`,
`lwjgl3/build/construo/macM1/roast`, and
`lwjgl3/build/construo/macX64/roast`

## Automated Releases

Every push to `main` triggers the workflow in `.github/workflows/release-on-main.yml`.
Each run:

- Builds the desktop app on Linux, Windows, and macOS
- Packages a native app image for each platform
- Creates a new GitHub Release tagged with a unique version derived from git history
- Uploads platform zip files to the release
