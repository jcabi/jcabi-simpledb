<img src="http://img.jcabi.com/logo-square.png" width="64px" height="64px" />

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
[![DevOps By Rultor.com](http://www.rultor.com/b/jcabi/jcabi-simpledb)](http://www.rultor.com/p/jcabi/jcabi-simpledb)

[![Build Status](https://travis-ci.org/jcabi/jcabi-simpledb.svg?branch=master)](https://travis-ci.org/jcabi/jcabi-simpledb)
[![PDD status](http://www.0pdd.com/svg?name=jcabi/jcabi-simpledb)](http://www.0pdd.com/p?name=teamed/jcabi/jcabi-simpledb)
[![Build status](https://ci.appveyor.com/api/projects/status/sdfe6qqji5hb4a4y/branch/master?svg=true)](https://ci.appveyor.com/project/yegor256/jcabi-simpledb/branch/master)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.jcabi/jcabi-simpledb/badge.svg)](http://www.javadoc.io/doc/com.jcabi/jcabi-simpledb)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-simpledb/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.jcabi/jcabi-simpledb)
[![Dependencies](https://www.versioneye.com/user/projects/561ac33fa193340f280010ef/badge.svg?style=flat)](https://www.versioneye.com/user/projects/561ac33fa193340f280010ef)

More details are here: [simpledb.jcabi.com](http://simpledb.jcabi.com/index.html)

Set of classes in `com.jcabi.simpledb`
is an object layer on top of
[AWS SDK for SimpleDB](http://aws.amazon.com/sdkforjava/).
For example, to read an item from your SimpleDB domain:

```java
public class Main {
  public static void main(String[] args) {
    Credentials credentials = new Credentials.Simple("AWS key", "AWS secret");
    Region region = new Region.Simple(credentials);
    Domain domain = region.domain("foo");
    Collection<Item> items = domain.select(
      new SelectRequest().withSelectExpression("SELECT * FROM foo")
    );
    for (Item item : items) {
      System.out.println(item.get("name"));
    }
  }
}
```

## Questions?

If you have any questions about the framework, or something doesn't work as expected,
please [submit an issue here](https://github.com/yegor256/jcabi/issues/new).

## How to contribute?

Fork the repository, make changes, submit a pull request.
We promise to review your changes same day and apply to
the `master` branch, if they look correct.

Please run Maven build before submitting a pull request:

```
$ mvn clean install -Pqulice
```
