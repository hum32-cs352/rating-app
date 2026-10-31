# C04 Assessment Module (CS-352, Section C)

First integration milestone for the Student Assessment Matrix. C04 connects A02 student identity with B05 reflection records through assessments and student records.

This module delivers a tested overview (3 x 1) and one Reflection activity's six-question detail view (3 x 6). It contains test data only. No marks, stars, bonus or verification are calculated.

## Offering context

| Item | Value |
|---|---|
| Department | DCS/UBIT |
| Course code | CS-352 |
| Semester | 2026-Semester-II |
| Section | C |
| Java target | 17 (`javac --release 17`) |

## Activities

| ID | Name | Purpose | Status |
|---|---|---|---|
| A1 | Reflection | FORMATIVE | Implemented (pilot, 3 x 1) |
| A2 | Midterm | SUMMATIVE | Test-only object in the scaling check |
| A3 | Final Lab Examination | SUMMATIVE | Test-only object in the scaling check |

A1 is a stable activity ID, not an array index. IDs are unique within one offering.

## Pilot students (test data)

| Label | Name | Seat number |
|---|---|---|
| S1 | Affan Rauf | B25110006008 |
| S2 | Aown Ali | B25110006026 |
| S3 | Abdul Rehman | B25110006004 |

Two reflection forms are received (S1, S2). S3's form is absent, so that cell is `null`.

## Dependencies

| Dependency | Owner | Recorded commit |
|---|---|---|
| A02 `Student`, `SectionName` (`src/domain/`) | Anas Arshad, PR #2 | 649211bdc5bfc6a815204c403c4530946c2e1e3d |
| B05 reflection files (`src/domain/reflection/`) | Aleeza, PR #4 | 66ba565c4a827ef58e988319c66727d7c4c0bd23 |

Required tools: Git and JDK 17 (or a newer JDK using `--release 17`).

```
git --version
java -version
javac -version
```

Packages used by the real code:

- A02: `domain` (for example `domain.Student`, `domain.SectionName`)
- B05: `domain.reflection` (for example `domain.reflection.Matrix`)
- C04: `pilot.c04.assessment`

## Repository layout

```
src/pilot/c04/assessment/     17 C04 Java files
handoff/C04/assessment/       handoff files (this folder)
  README.md
  sources.txt
  run_assessment.bat
  test-results.txt
  contributions.txt
  C04_responsibilities_edited.pdf
  C04_UML_updated.drawio
  C04_API_REGISTER.xlsx
```

`sources.txt` lists 25 files once each: 2 from A02, 6 from B05 and 17 from C04.

## How to run

1. Clone the repository and switch to the correct branch.
   - Before the approved merge: `git switch team/C04-assessment`
   - After the approved merge: `git switch integration`
2. Open `handoff/C04/assessment/` in File Explorer.
3. Double-click `run_assessment.bat`.

Or from Command Prompt at the repository root:

```
handoff\C04\assessment\run_assessment.bat
```

The script resolves the repository root from its own location, deletes only `out/C04-assessment`, compiles with `javac --release 17`, runs `AssessmentChecks`, and only if all checks pass runs `AssessmentDriver`. It stops on any failure and exits with a nonzero code. No absolute paths are used. `out/` is generated and ignored by Git.

## Expected output

`AssessmentChecks` prints PASS only after each check class returns successfully. `AssessmentDriver` then prints:

- the 3 x 1 overview: official seat numbers, A1 Reflection (FORMATIVE), "Received / verification pending" for received forms, "Not recorded" when the submission time is unknown, and `-` for the missing form
- the 3 x 6 A1 detail: Affan consultations = `None`, Aown consultations = `Once`, and six `-` cells for Abdul Rehman

`-` means a missing answer or form (null). `None` is a chosen answer, not missing data.

## Checks

| Check class | Owner | Covers |
|---|---|---|
| `ScheduleCheck` | Abdul Rehman | valid, null, equal and reversed times |
| `ReflectionCheck` | Asad | name, ID, context, schedule, purposes, null purpose |
| `RecordCheck` | Abeer | shared references, polymorphic evidence access |
| `LinkCheck` | Anas Nasir | matching and mismatching records |
| `MatrixCheck` | Moonis Nasir | overview, selection, purpose filters, scaling |
| `AssessmentChecks` | Aown | runs all five, prints PASS only on success |

Full acceptance results (T01 to T14) are in `test-results.txt`.

## Changes made

| Change | Reason | PR discussion link |
|---|---|---|
| B05 types are imported from `domain.reflection`, not `pilot.b05` | The merged B05 code uses package `domain.reflection` | https://github.com/hum32-cs352/rating-app/pull/4#issuecomment-6542944325 |

## Team

| Member | Role |
|---|---|
| Abdul Rehman (6004) | Assessment, Schedule, ScheduleCheck, API register |
| Asad (6090) | Purpose, Direct, Indirect, Reflection, ReflectionCheck |
| Abeer (6006) | AssessmentRecord, RecordCheck |
| Anas Nasir (6087) | ReflectionRecord, LinkCheck, UML |
| Moonis Nasir (6110) | AssessmentMatrix, AssessmentView, MatrixCheck |
| Affan Rauf (6008) | AssessmentDriver, build files, coordination, publisher |
| Aown (6026) | AssessmentChecks, independent testing |

Supervisor: Humera Tariq.
