`REOpenChord` is part of a DFS project based on `Open Chord`.

## Open Chord

`Open Chord` is released under GNU General Public License. A copy of it is
contained in the archives in a file named license.txt.
A copy of the license can be found at http://www.gnu.org/copyleft/gpl.html.


## Requirements

* `Java SE 8` (`openjdk-8` is OK, because `jenetics` relies extensively on `Java SE 8`).
* `Maven` (recommended, http://maven.apache.org) to compile `REOpenChord`.

## Compilation

Just run the commands below. (`maven` requried. Otherwise you should compile those `.java` files manully)

```sh
mvn compile
```
## Configuration

If you wants to join an existing Chord overlay network, a file named `bootstrapURL` must be put in the current directory where this program runs. The content of that file should be the URL of a node that has already in the Chord overlay network.

## Run

```sh
mvn dependency:copy-dependencies
java -cp target/classes:src/main/resources:target/dependency/* cn.edu.ustc.center.Center
```

## Documentation

The api info (javadoc) can be generated with the following command

```sh
mvn javadoc:javadoc
```

Then you can find them in `target/site/apidocs`.
