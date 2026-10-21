# rating-app: C04 Launch 01 Shared Java Starter

Object Oriented Concepts and Programming, University of Karachi. Section C, Team 04.

This is a minimal, JDK-only Java 17 starter that every team will later clone and build. Running it prints a single success message.

## 1. Requirements

Install:

- Git
- JDK 17, or a newer JDK that supports `--release 17`

Verify the installation:

```text
git --version
java -version
javac -version
```

## 2. Get the project

```text
git clone https://github.com/hum32-cs352/rating-app.git
cd rating-app
```

Then switch to the correct branch:

- **Before the pilot PR is merged**, use the pilot branch:

  ```text
  git switch team/C04-bootstrap
  ```

- **After the approved merge**, use the shared branch:

  ```text
  git switch integration
  ```

## 3. Run

1. Open the `rating-app` repository folder.
2. Double click `run.bat`.
3. Let the script compile and run the application.
4. Check that the output is exactly:

```text
Hum32@CS352 starter runs successfully.
```

If the build or run fails, the script shows `BUILD OR RUN FAILED` instead. Do not treat a failed build as a pass.

No absolute paths or manual edits are needed. The project works from any folder, including one whose path contains spaces.

## 4. Generated files

`out/` is created by the build every time `run.bat` runs. It holds the compiled `.class` files. It is generated output and must not be committed. `.gitignore` already excludes `/out/` and `*.class`.

The Java files that are compiled are listed, one per line, in `src/sources.txt`.
