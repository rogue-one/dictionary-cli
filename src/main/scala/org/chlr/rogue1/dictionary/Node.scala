package org.chlr.rogue1.dictionary

import scala.annotation.tailrec
import scala.collection.mutable

sealed trait Node  {

  val children: Children = mutable.HashMap()

  def addLeafNode(value: Char, desc: String): Boolean = {
    children.get(value) match {
      case Some(x: LeafNode) => false
      case _ => val node = LeafNode(value, this, desc); children.put(value, node); true
    }
  }

  def addBranchNode(value: Char): BaseNode = {
    children.get(value) match {
      case Some(x: BaseNode) => x
      case None => val node = BranchNode(value, this); children.put(value, node); node;
    }
  }


  def fetch(word: List[Char], trace: List[BaseNode]): Option[BaseNode] = {
    word match {
      case head :: Nil => children.get(head)
      case head :: tail => children.get(head) match { case Some(x) => x.fetch(tail, x :: trace) case None => None }
      case Nil => None
    }
  }

  def walk(trace: List[Node]): List[List[Node]] = {
    children.values.toList match  {
      case Nil => List(this :: trace)
      case list =>  list.flatMap(_.walk(this :: trace))
    }
  }

  def queryPrefix: List[String] = {
    this.walk(Nil).map(_.collect({ case x: BaseNode => x.value}).reverse.mkString(""))
  }

}

class RootNode extends Node {

  /**
   * a word to dictionary
   * @param word
   * @param desc
   */
  def add(word: String, desc: String): Boolean = {
    @tailrec def process(node: Node = this, word: List[Char], desc: String): Boolean = {
      word match {
        case Nil => false
        case head :: Nil => node.addLeafNode(head, desc)
        case head :: tail => process(node.addBranchNode(head), tail, desc)
      }
    }
    process(this, word.toList, desc)
  }

}

abstract class BaseNode(val value: Char,
                        val parent: Node) extends Node {

  def word: List[Char] = {
    def walk(path: List[Char]): List[Char] = {
      parent match {
        case x: RootNode => path
        case x: BaseNode => x.word ++ path
      }
    }
    walk(Nil)
  }

}


case class BranchNode(override val value: Char,
                      override val parent: Node) extends BaseNode(value, parent)

case class LeafNode(override val value: Char,
                    override val parent: Node,
                    val description: String) extends BaseNode(value, parent)

