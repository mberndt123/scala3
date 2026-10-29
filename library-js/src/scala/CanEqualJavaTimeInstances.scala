package scala

import language.experimental.captureChecking

/** Scala.js counterpart of the JVM `CanEqualJavaTimeInstances`.
 *
 *  `java.time` is not available in the Scala.js javalib. The JVM instances are all `inline`, so
 *  code compiled against them never refers to them at run time, and this trait can be empty.
 */
private[scala] trait CanEqualJavaTimeInstances
