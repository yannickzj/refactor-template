# *Jrefactoring*: parameterization refactoring tool

## Summary

|       Benchmark   | Total tests | Detected clone pairs | Parameterized clone pairs |Effective candidates | Refactorable | Refactoring ratio |
|:-----------------:|:-----------:|:--------------------:|:-------------------------:|:-------------------:|:------------:|:-----------------:|
| jfreechart-1.0.10 |     3934    |         272          |           71              |          62         |      51      |        82.3%      |
| gson-2.8.5        |     1050    |          39          |           16              |          14         |      10      |        71.4%      |

- Detected clone pairs: clone pairs directly detected from clone detection tools;
- Parameterized clone pairs: parameterizable clone candidates identified by *Jrefactoring*, still including candidates which are not test cases, and repeated cases;
- Effective candidates: test clone pairs without repetition;
- Refactorable: test clone pairs that are successfully refactored by *Jrefactoring* (passed with same coverage);
- Refactoring ratio: refactorable / effective candidates.


## Common launch config

Workspace Data Location:
workspace

VM arguments: 
-Xms128m -Xmx8192m

Working directory:
/home/yannick/workspace/eclipse

## Jfreechart-1.0.10

skipped cases:
- TLE, *TestEquals* (JDeodorant): 2356,2889,5249,5315,8572

<!---
- repeated refactoring (JDeodorant): 7821
- no method parameter mapping (JDeodorant): 8816
- private field access in different files: 1674,3715
- not test code: 8097
--->

Program arguments:
-p jfreechart-1.0.10
-x refactor-template/results/jfreechart-1.0.10/jfreechart-1.0.10-selected.xls
-m ANALYZE_EXISTING
-l
-s 2356,2889,5249,5315,8572
-pkg org.jfree
-ac

## Gson-2.8.5

Program arguments:
-p gson
-x refactor-template/results/gson-2.8.5/gson-2.8.5-deckard-selected.xls
-m ANALYZE_EXISTING 
-l
-pkg com.google.gson
-ac

## Alternative for data parameterization refactoring

*JUnit Theories* could be better choice when test clone pairs only have data-parameterizable difference:

- [Javadoc](https://junit.org/junit4/javadoc/4.12/org/junit/experimental/theories/Theories.html)
- [Github Wiki](https://github.com/junit-team/junit4/wiki/theories)
