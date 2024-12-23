# Releasing

1. Change the version in `gradle.properties` to a non-SNAPSHOT version
2. Update the `CHANGELOG.md` for the new release
3. Update the `README.md` with the new version
4. `git checkout -b release-X.Y.Z` (where X.Y.Z is the new version)
5. `git commit -am "Release X.Y.Z"`
6. `git push`; make a PR; merge PR
7. `git checkout main && git pull`
8. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version)
9. Update the `gradle.properties` to the next SNAPSHOT version (increment patch by 1, e.g. `1.0.0` → `1.0.1-SNAPSHOT`)
10. `git commit -am "Prepare next development version"`
11. `git push && git push --tags`
12. Visit [the GitHub releases page](https://github.com/ryanmoelter/magellanx/releases) and create a new release, copying the changelog from `CHANGELOG.md`
13. Visit [the Jitpack site](https://jitpack.io/#com.ryanmoelter/magellanx) and check that the new release shows up

## Publish to local maven repo

1. Run `./gradlew publishToMavenLocal`.
2. In the other project, add `mavenLocal()` as a repository (likely in `allProjects.repositories` of the root `build.gradle` file).
3. Update `com.ryanmoelter.magellanx:magellanx-compose:X.Y.Z` to `com.ryanmoelter.magellanx:magellanx-compose:SNAPSHOT_VERSION`, where `SNAPSHOT_VERSION` is the `VERSION_NAME` defined in this project's `./gradle.properties`.

## Publish from a PR

[the Jitpack site](https://jitpack.io/#com.ryanmoelter/magellanx) provides builds for each branch. They take a while to build, so for quick iteration I recommend using `mavenLocal` as shown above.

1. Push a branch to this repo
2. Update your dependency version to `{branch-name}-SNAPSHOT`, where `{branch-name}` is your branch's name
3. Wait for Jitpack to build, which might take ~10 minutes
