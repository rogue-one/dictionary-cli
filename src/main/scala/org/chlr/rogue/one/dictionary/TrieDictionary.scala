package org.chlr.rogue.one.dictionary

class TrieDictionary {

  def addWord(word: String, desc: String): Unit = {
    RootNode.add(word.toList, desc)
  }

  def retrieve(word: String): Unit = {
    RootNode.fetch(word.toList, Nil) match {
      case Some(x: EndNode) => println(x.description)
      case _ => println(s"no entry found for word $word")
    }
  }

  def query(prefix: String): Unit = {

  }

}
