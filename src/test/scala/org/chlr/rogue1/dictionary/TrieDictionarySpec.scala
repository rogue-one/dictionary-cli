package org.chlr.rogue1.dictionary

import org.scalacheck.Gen
import org.scalacheck.Prop.BooleanOperators


class TrieDictionarySpec extends TestSpec {


  "TrieDictionary" must "insert and fetch words" in {
    val dictionary = new TrieDictionary(new RootNode)
    forAll {
      (word: String, desc: String) => {
        (word.length > 0 && desc.length > 0) ==> {
          val result = dictionary.addWord(word, desc)
          (!result || dictionary.retrieve(word).contains(desc))
        }
      }
    }
  }

  it must "query word matching prefixes" in {
    val dictionary = new TrieDictionary(new RootNode)
    val customGen = for {
      prefix <- Gen.asciiPrintableStr
      list <- Gen.nonEmptyContainerOf[List, String](Gen.asciiPrintableStr.suchThat(_.length > 3))
    } yield (prefix, list.map(x => s"$prefix$x"))
    forAll(customGen) {
      case (prefix: String, words: List[String]) =>
        words.foreach(dictionary.addWord(_, ""))
        dictionary.query(prefix) == words
    }
  }

}
