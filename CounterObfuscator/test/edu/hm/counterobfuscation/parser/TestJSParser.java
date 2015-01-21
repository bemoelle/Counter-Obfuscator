///**
// * 
// */
//package edu.hm.counterobfuscation.parser;
//
//import static org.junit.Assert.*;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.commons.lang3.StringEscapeUtils;
//import org.junit.Test;
//
//import edu.hm.counterobfuscator.parser.IJSParser;
//import edu.hm.counterobfuscator.parser.JSParserFactory;
//import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
//import edu.hm.counterobfuscator.types.Function;
//import edu.hm.counterobfuscator.types.Variable;
//
///**
// * @author Benjamin Moellerke <bemoelle@gmail.com>
// * @date 02.01.2015
// * 
// * 
// */
//public class TestJSParser {
//
//	@Test
//	public void VariableTest() throws IOException {
//		IJSParser jsParser = JSParserFactory.create("varTest");
//
//		assertEquals(jsParser.getTokens().size(), 41);
//
//		List<Variable> vars = jsParser.getTypesOfToken(TOKENTYPE.VAR);
//
//		assertEquals(vars.size(), 2);
//
//		Variable v0 = vars.get(0);
//		Variable v1 = vars.get(1);
//
//		// -------- v0 --------
//		assertEquals(v0.getStartPos(), 1);
//		assertEquals(v0.getEndPos(), 18);
//		assertEquals(v0.getName(), "NMeZD");
//		assertEquals(v0.getValue(), "'krkeIplIaMcMIe'.replace(/[kIYM]/g,'')");
//
//		// -------- v1 --------
//		assertEquals(v1.getStartPos(), 21);
//		assertEquals(v1.getEndPos(), 39);
//		assertEquals(v1.getName(), "DubWtR");
//		assertEquals(v1.getValue(), "'ufIrIIoumuCShIaruCuoSdIe'[NMeZD](/[IuS]/g,'')");
//
//	}
//
//	@Test
//	public void VariableTest2() throws IOException {
//		IJSParser jsParser = JSParserFactory.create("varTest2");
//
//		assertEquals(jsParser.getTokens().size(), 63);
//
//		List<Variable> vars = jsParser.getTypesOfToken(TOKENTYPE.VAR);
//
//		assertEquals(vars.size(), 10);
//
//		Variable v0 = vars.get(0);
//		Variable v1 = vars.get(1);
//		Variable v2 = vars.get(2);
//		Variable v3 = vars.get(3);
//		Variable v4 = vars.get(4);
//		Variable v5 = vars.get(5);
//		Variable v6 = vars.get(6);
//		Variable v7 = vars.get(7);
//		Variable v8 = vars.get(8);
//		Variable v9 = vars.get(9);
//
//		// -------- v0 --------
//		assertEquals(v0.getStartPos(), 1);
//		assertEquals(v0.getEndPos(), 18);
//		assertEquals(v0.getName(), "test0");
//		assertEquals(v0.getValue(), "'krkeIplIaMcMIe'.replace(/[kIYM]/g,'')");
//
//		// -------- v1 --------
//		assertEquals(v1.getStartPos(), 20);
//		assertEquals(v1.getEndPos(), 22);
//		assertEquals(v1.getName(), "test10");
//		assertEquals(v1.getValue(), "10");
//
//		// -------- v2 --------
//		assertEquals(v2.getStartPos(), 25);
//		assertEquals(v2.getEndPos(), 25);
//		assertEquals(v2.getName(), "test1");
//		assertEquals(v2.getValue(), "undefined");
//
//		// -------- v3 --------
//		assertEquals(v3.getStartPos(), 27);
//		assertEquals(v3.getEndPos(), 27);
//		assertEquals(v3.getName(), "test2");
//		assertEquals(v3.getValue(), "undefined");
//
//		// -------- v4 --------
//		assertEquals(v4.getStartPos(), 30);
//		assertEquals(v4.getEndPos(), 32);
//		assertEquals(v4.getName(), "test3");
//		assertEquals(v4.getValue(), "100");
//
//		// -------- v5 --------
//		assertEquals(v5.getStartPos(), 35);
//		assertEquals(v5.getEndPos(), 37);
//		assertEquals(v5.getName(), "test4");
//		assertEquals(v5.getValue(), "100");
//
//		// -------- v6 --------
//		assertEquals(v6.getStartPos(), 39);
//		assertEquals(v6.getEndPos(), 39);
//		assertEquals(v6.getName(), "test5");
//		assertEquals(v6.getValue(), "undefined");
//
//		// -------- v7 --------
//		assertEquals(v7.getStartPos(), 42);
//		assertEquals(v7.getEndPos(), 44);
//		assertEquals(v7.getName(), "test6");
//		assertEquals(v7.getValue(), "100");
//
//		// -------- v8 --------
//		assertEquals(v8.getStartPos(), 46);
//		assertEquals(v8.getEndPos(), 48);
//		assertEquals(v8.getName(), "test7");
//		assertEquals(v8.getValue(), "100");
//
//		// -------- v9 --------
//		assertEquals(v9.getStartPos(), 51);
//		assertEquals(v9.getEndPos(), 51);
//		assertEquals(v9.getName(), "test8");
//		assertEquals(v9.getValue(), "undefined");
//
//		// TODO TEST global
//
//	}
//
//	@Test
//	public void FunctionTest() throws IOException {
//		IJSParser jsParser = JSParserFactory.create("functionTest");
//
//		assertEquals(jsParser.getTokens().size(), 103);
//
//		List<Function> function = jsParser.getTypesOfToken(TOKENTYPE.FUNCTION);
//
//		assertEquals(function.size(), 3);
//
//		Function f0 = function.get(0);
//		Function f1 = function.get(1);
//		Function f2 = function.get(2);
//
//		// -------- f0 --------
//		assertEquals(f0.getStartPos(), 22);
//		assertEquals(f0.getEndPos(), 79);
//		assertEquals(f0.getName(), "NewObject");
//		assertEquals(f0.getHead(), "(_0x61fex2,_0x61fex9)");
//		assertEquals(
//				f0.getBoby(),
//				"{var_0x61fex3=0;this[_0x44c5[0]]=function(_0x61fex4){_0x61fex3++;alert(_0x61fex9+_0x61fex2+_0x61fex4);};this[_0x44c5[1]]=function(){return_0x61fex3;};}");
//
//		// -------- f1 --------
//		assertEquals(f1.getStartPos(), 43);
//		assertEquals(f1.getEndPos(), 60);
//		assertEquals(f1.getName(), "");
//		assertEquals(f1.getHead(), "(_0x61fex4)");
//		assertEquals(f1.getBoby(), "{_0x61fex3++;alert(_0x61fex9+_0x61fex2+_0x61fex4);}");
//
//		// -------- f2 --------
//		assertEquals(f2.getStartPos(), 71);
//		assertEquals(f2.getEndPos(), 77);
//		assertEquals(f2.getName(), "");
//		assertEquals(f2.getHead(), "()");
//		assertEquals(f2.getBoby(), "{return_0x61fex3;}");
//
//	}
//
//	@Test
//	public void FunctionTest2() throws IOException {
//		IJSParser jsParser = JSParserFactory.create("functionTest2");
//
//		assertEquals(jsParser.getTokens().size(), 143);
//
//		List<Function> function = jsParser.getTypesOfToken(TOKENTYPE.FUNCTION);
//
//		assertEquals(function.size(), 1);
//
//		Function f0 = function.get(0);
//
//		// -------- f0 --------
//		assertEquals(f0.getStartPos(), 102);
//		assertEquals(f0.getEndPos(), 108);
//		assertEquals(f0.getName(), "");
//		assertEquals(f0.getHead(), "()");
//		assertEquals(f0.getBoby(), "{returnthis;}");
//
//	}
//
//	@Test
//	public void PackedFunctionTest() throws IOException {
//		IJSParser jsParser = JSParserFactory.create("packedTest");
//
//		assertEquals(jsParser.getTokens().size(), 332);
//
//		List<Function> function = jsParser.getTypesOfToken(TOKENTYPE.FUNCTION);
//
//		assertEquals(function.size(), 4);
//
//		Function f0 = function.get(0);
//		Function f1 = function.get(1);
//		Function f2 = function.get(2);
//		Function f3 = function.get(3);
//
//		// -------- f0 --------
//		assertEquals(f0.getStartPos(), 4);
//		assertEquals(f0.getEndPos(), 155);
//		assertEquals(f0.getName(), "");
//		assertEquals(f0.getHead(), "(p,a,c,k,e,r)");
//		assertEquals(
//				StringEscapeUtils.unescapeJava(f0.getBoby()),
//				"{e=function(c){returnc.toString(a)};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){returnr[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(newRegExp('\\b'+e(c)+'\\b','g'),k[c]);returnp}");
//
//		// -------- f1 --------
//		assertEquals(f1.getStartPos(), 21);
//		assertEquals(f1.getEndPos(), 32);
//		assertEquals(f1.getName(), "");
//		assertEquals(f1.getHead(), "(c)");
//		assertEquals(f1.getBoby(), "{returnc.toString(a)}");
//
//		// -------- f2 --------
//		assertEquals(f2.getStartPos(), 77);
//		assertEquals(f2.getEndPos(), 86);
//		assertEquals(f2.getName(), "");
//		assertEquals(f2.getHead(), "(e)");
//		assertEquals(f2.getBoby(), "{returnr[e]}");
//
//		// -------- f3 --------
//		assertEquals(f3.getStartPos(), 92);
//		assertEquals(f3.getEndPos(), 100);
//		assertEquals(f3.getName(), "");
//		assertEquals(f3.getHead(), "()");
//		assertEquals(StringEscapeUtils.unescapeJava(f3.getBoby()), "{return'\\w+'}");
//
//	}
//}
