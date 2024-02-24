<img src="https://www.jcabi.com/logo-square.svg" width="64px" height="64px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![Managed by Zerocracy](https://www.0crat.com/badge/C3RUBL5H9.svg)](https://www.0crat.com/p/C3RUBL5H9)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-simpledb)](http://www.rultor.com/p/jcabi/jcabi-simpledb)

[![mvn](https://github.com/jcabi/jcabi-simpledb/actions/workflows/mvn.yml/badge.svg)](https://github.com/jcabi/jcabi-simpledb/actions/workflows/mvn.yml)
[![PDD status](http://www.0pdd.com/svg?name=jcabi/jcabi-simpledb)](http://www.0pdd.com/p?name=jcabi/jcabi-simpledb)
[![Javadoc](https://javadoc.io/badge/com.jcabi/jcabi-simpledb.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-simpledb)
[![jpeek report](https://i.jpeek.org/com.jcabi/jcabi-simpledb/badge.svg)](https://i.jpeek.org/com.jcabi/jcabi-simpledb/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-simpledb/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-simpledb)
[![codecov](https://codecov.io/gh/jcabi/jcabi-simpledb/branch/master/graph/badge.svg)](https://codecov.io/gh/jcabi/jcabi-simpledb)

More details are here: [simpledb.jcabi.com](https://simpledb.jcabi.com/index.html)

Set of classes in `com.jcabi.simpledb`
is an object layer on top of
[AWS SDK for SimpleDB](https://aws.amazon.com/sdkforjava/).
For example, to read an item from your SimpleDB domain:

```java
public class Main {
    public static void main(final String[] args) {
        final Credentials credentials = new Credentials.Simple("AWS key", "AWS secret");
        final Region region = new Region.Simple(credentials);
        final Domain domain = region.domain("foo");
        final Collection<Item> items = domain.select(
            new SelectRequest().withSelectExpression("SELECT * FROM foo")
        );
        for (final Item item : items) {
            System.out.println(item.get("name"));
        }
    }
}
```

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
