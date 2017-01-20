package com.movesol.jparsec.examples.java.parser;

import static com.movesol.jparsec.examples.java.parser.TerminalParserTest.assertFailure;
import static com.movesol.jparsec.examples.java.parser.TerminalParserTest.assertResult;

import java.util.Arrays;

import org.junit.Test;

import com.movesol.jparsec.Parser;
import com.movesol.jparsec.examples.java.ast.type.ArrayTypeLiteral;
import com.movesol.jparsec.examples.java.ast.type.SimpleTypeLiteral;
import com.movesol.jparsec.examples.java.ast.type.TypeLiteral;
import com.movesol.jparsec.examples.java.parser.TerminalParser;
import com.movesol.jparsec.examples.java.parser.TypeLiteralParser;

/**
 * Unit test for [@link TypeLiteralParser}.
 * 
 * @author Ben Yu
 */
public class TypeLiteralParserTest {

  @Test
  public void testArrayOf() {
    SimpleTypeLiteral intType =
        new SimpleTypeLiteral(Arrays.asList("int"), TypeLiteralParser.EMPTY_TYPE_ARGUMENT_LIST);
    TerminalParserTest.assertToString(ArrayTypeLiteral.class, "int[]",
        TerminalParser.parse(TypeLiteralParser.ARRAY_OF, "[]").map(intType));
  }

  @Test
  public void testTypeLiteral() {
    Parser<TypeLiteral> parser = TypeLiteralParser.TYPE_LITERAL;
    assertResult(parser, "int", SimpleTypeLiteral.class, "int");
    assertResult(parser, "a.b.c", SimpleTypeLiteral.class, "a.b.c");
    assertResult(parser, "java.util.Map<K, V>", SimpleTypeLiteral.class, "java.util.Map<K, V>");
    assertResult(parser, "Pair<A, Pair<A,B>>", SimpleTypeLiteral.class, "Pair<A, Pair<A, B>>");
    assertResult(parser, "Pair<?, ?>", SimpleTypeLiteral.class, "Pair<?, ?>");
    assertResult(parser, "List<? extends List<?>>",
        SimpleTypeLiteral.class, "List<? extends List<?>>");
    assertFailure(parser, "?", 1, 1);
    assertFailure(parser, "List<? extends ?>", 1, 16);
    assertResult(parser, "Pair<? extends A, ? super B>",
        SimpleTypeLiteral.class, "Pair<? extends A, ? super B>");
    assertResult(parser, "int[]", ArrayTypeLiteral.class, "int[]");
    assertResult(parser, "Pair<A, Pair<A,B>>[]", ArrayTypeLiteral.class, "Pair<A, Pair<A, B>>[]");
    assertResult(parser, "int[][]", ArrayTypeLiteral.class, "int[][]");
  }

  @Test
  public void testElementTypeLiteral() {
    Parser<TypeLiteral> parser = TypeLiteralParser.ELEMENT_TYPE_LITERAL;
    assertResult(parser, "int", SimpleTypeLiteral.class, "int");
    assertFailure(parser, "int[]", 1, 4);
  }

  @Test
  public void testArrayTypeLiteral() {
    Parser<TypeLiteral> parser = TypeLiteralParser.ARRAY_TYPE_LITERAL;
    assertResult(parser, "int[]", ArrayTypeLiteral.class, "int[]");
    assertFailure(parser, "int", 1, 4);
  }
}
