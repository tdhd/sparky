# Sparky: Apache Spark samples

Sparky is a collection of Apache Spark (http://spark.apache.org/) samples. It also contains code samples for applications of the edX course **BerkeleyX:** CS100.1x Introduction to Big Data with Apache Spark.

# To run

To run the samples, you need:

* sbt 0.13.7+
* java 1.7

```
$ git clone https://github.com/tdhd/sparky.git
$ cd sparky
$ sbt
> run
```

# List of Examples

- Wikipedia wordcount sample (`> runMain io.github.tdhd.sparky.WordCount`)
- Local files streaming wordcount sample (`> runMain io.github.tdhd.sparky.StreamingWordCount`)

