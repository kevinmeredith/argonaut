package com.ephox.argonaut

import org.scalacheck.Prop._
import util.parsing.input.CharSequenceReader
import org.scalacheck.Properties
import Data._
import JsonParser._
import JsonPrinter._

object JsonParserTest extends Properties("JsonParser") {
  val subject = new JsonParser

  property("null parses ok") =
          forAll((s: SometimesNullString) => (s == SometimesNullString("null")) == p(subject.jnull, s.s).successful)

  property("null parses to null") =
          forAll((s: SometimesNullString) => s != SometimesNullString("null") || p(subject.jnull, s.s).get.isNull)

  property("boolean parses ok") =
          forAll((s: SometimesBoolString) => (List("true", "false").contains(s.s) == p(subject.jboolean, s.s).successful))

  property("boolean parses to bool") =
          forAll((s: SometimesBoolString) => (List("true", "false").contains(s.s) ==> p(subject.jboolean, s.s).get.isBool))

  // FIX unicode escaped chars? don't think they are being generated at the moment
  /*
  // todo failing tests
  property("all that encodes can be decoded") =
          forAll({(j: Json) =>
              val parsed = parse(pretty(j))
              parsed.successful && parsed.get == j
            })

  property("known json decodes") = 
          forAll({(c: CannedData) =>
              parse(c.s).successful
            })

  property("known json, re-encodes") =
          forAll({(c: CannedData) =>
              val first = parse(c.s).get
              val second = parse(pretty(first)).get
              first == second
            })
  */

  def p(k: subject.Parser[Json], s: String) =
    k(new CharSequenceReader(s))
}