package org.chlr.rogue.one
import scala.collection.mutable
import java.lang.ref.{PhantomReference, SoftReference}

package object dictionary {
  type Children = mutable.HashMap[Char, BaseNode]
}
