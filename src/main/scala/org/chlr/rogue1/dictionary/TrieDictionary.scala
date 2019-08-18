package org.chlr.rogue1.dictionary

class TrieDictionary(val rootNode: RootNode) {

  def addWord(word: String, desc: String): Boolean = {
    rootNode.add(word, desc)
  }

  def retrieve(word: String): Option[String] = {
    rootNode.fetch(word.toList, Nil) match {
      case Some(x: LeafNode) => Some(x.description)
      case _ => None
    }
  }

  def query(prefix: String): List[String] = {
    rootNode.fetch(prefix.toList, Nil) match {
      case Some(x) => x.queryPrefix
      case None => Nil
    }
  }

}
