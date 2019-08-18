package org.chlr.rogue.one.dictionary

import org.chlr.rogue.one.dictionary.RootNode.children

import scala.annotation.tailrec
import scala.collection.mutable

sealed trait Node  {
  protected val children: Children = mutable.HashMap()

  final def add(word: List[Char], desc: String): Unit = {
    word match {
      case Nil => ()
      case head :: Nil => addChar(head, Some(desc))
      case head :: tail => addChar(head, None).add(tail, desc)
    }
  }

  def addChar(value: Char,
              desc: Option[String]): BaseNode = {
    (desc, children.get(value)) match {
      case (Some(_), Some(y: EndNode)) =>  y
      case (Some(x), _) => val node = EndNode(value, this, x); children.put(value, node); node
      case (None, Some(y)) => y
      case (None, None) => val node = NonEndNode(value, this); children.put(value, node); node
    }
  }


  def fetch(word: List[Char], trace: List[BaseNode]): Option[BaseNode] = {
    word match {
      case head :: Nil => children.get(head) match { case Some(x) => Some(x)  case None => None }
      case head :: tail => children.get(head) match { case Some(x) => fetch(tail, x :: trace) case None => None }
      case Nil => None
    }
  }

  def words: List[String] = {
    children.toList
  }

  def query(word: List[Char]) = {
    fetch(word, Nil) match {
      case Some(x: EndNode) => "" :: Nil
      case Some()
    }
  }



}

object RootNode extends Node

abstract class BaseNode(val value: Char,
                        val parent: Node) extends Node {

  def word: List[Char] = {
    def walk(path: List[Char]): List[Char] = {
      parent match {
        case RootNode => path
        case x: BaseNode => x.word ++ path
      }
    }
    walk(Nil)
  }

}


case class NonEndNode(override val value: Char,
                      override val parent: Node) extends BaseNode(value, parent)

case class EndNode(override val value: Char,
                   override val parent: Node,
                   val description: String) extends BaseNode(value, parent)

