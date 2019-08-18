package org.chlr.rogue1.dictionary

import org.scalacheck.Gen


class TrieDictionarySpec extends TestSpec {


  "TrieDictionary" must "insert and fetch words" in {
    val dictionary = new TrieDictionary(new RootNode)
    forAll {
      (word: String, desc: String) => {
        whenever(word.length > 0 && desc.length > 0) {
          val result = dictionary.addWord(word, desc)
          (!result || dictionary.retrieve(word).contains(desc)) mustBe true
        }
      }
    }
  }

  it must "query word matching prefixes" in {
    val dictionary = new TrieDictionary(new RootNode)

    def genWordSize(size: Int): Gen[String] = Gen.listOfN(size, Gen.alphaChar).map(_.mkString)

    val customGen = genWordSize(3)
      .flatMap(prefix => Gen.listOfN(6, genWordSize(10)).map(x => prefix -> x.map(prefix.concat)))
      .suchThat({ case (prefix, words) => words.forall(_.length > 0) })
    forAll(customGen) {
      case (prefix: String, words: List[String]) =>
        words.foreach(dictionary.addWord(_, ""))
        dictionary.query(prefix) must contain only (words.map(_.substring(3)): _*)
    }
  }


}
