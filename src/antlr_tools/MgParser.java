package antlr_tools;// Generated from Mg.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MgParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, LogicConstant=33, PosIntegerConstant=34, StringConstant=35, 
		LineComment=36, BlockComment=37, WhiteSpace=38, NewLine=39, Bool=40, Int=41, 
		String=42, Void=43, Null=44, True=45, False=46, If=47, Else=48, For=49, 
		While=50, Break=51, Continue=52, Return=53, New=54, Class=55, This=56, 
		Identifier=57;
	public static final int
		RULE_prog = 0, RULE_dec = 1, RULE_varDec = 2, RULE_varDeclaratorList = 3, 
		RULE_varDeclarator = 4, RULE_functDec = 5, RULE_functDecParaList = 6, 
		RULE_functDecPara = 7, RULE_classDec = 8, RULE_classBody = 9, RULE_classBodyDec = 10, 
		RULE_constructorDec = 11, RULE_stm = 12, RULE_expStm = 13, RULE_blockStm = 14, 
		RULE_varDecStm = 15, RULE_ifStm = 16, RULE_whileStm = 17, RULE_forStm = 18, 
		RULE_forControl = 19, RULE_forInit = 20, RULE_forUpdate = 21, RULE_breakStm = 22, 
		RULE_continueStm = 23, RULE_returnStm = 24, RULE_emptyStm = 25, RULE_exp = 26, 
		RULE_primaryExp = 27, RULE_literal = 28, RULE_arrayAccessor = 29, RULE_arguments = 30, 
		RULE_expList = 31, RULE_creator = 32, RULE_arrayCreatorDim = 33, RULE_integerLiteral = 34, 
		RULE_stringLiteral = 35, RULE_logicLiteral = 36, RULE_type = 37, RULE_simpleType = 38, 
		RULE_primitiveType = 39, RULE_userType = 40, RULE_arrayType = 41, RULE_arrayDimDecList = 42, 
		RULE_arrayDimDec = 43;
	public static final String[] ruleNames = {
		"prog", "dec", "varDec", "varDeclaratorList", "varDeclarator", "functDec", 
		"functDecParaList", "functDecPara", "classDec", "classBody", "classBodyDec", 
		"constructorDec", "stm", "expStm", "blockStm", "varDecStm", "ifStm", "whileStm", 
		"forStm", "forControl", "forInit", "forUpdate", "breakStm", "continueStm", 
		"returnStm", "emptyStm", "exp", "primaryExp", "literal", "arrayAccessor", 
		"arguments", "expList", "creator", "arrayCreatorDim", "integerLiteral", 
		"stringLiteral", "logicLiteral", "type", "simpleType", "primitiveType", 
		"userType", "arrayType", "arrayDimDecList", "arrayDimDec"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "','", "'='", "'('", "')'", "'{'", "'}'", "'++'", "'--'", 
		"'+'", "'-'", "'!'", "'~'", "'*'", "'/'", "'%'", "'<<'", "'>>'", "'>'", 
		"'>='", "'<'", "'<='", "'=='", "'!='", "'&'", "'^'", "'|'", "'&&'", "'||'", 
		"'.'", "'['", "']'", null, null, null, null, null, null, null, "'bool'", 
		"'int'", "'string'", "'void'", "'null'", "'true'", "'false'", "'if'", 
		"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", 
		"'class'", "'this'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, "LogicConstant", 
		"PosIntegerConstant", "StringConstant", "LineComment", "BlockComment", 
		"WhiteSpace", "NewLine", "Bool", "Int", "String", "Void", "Null", "True", 
		"False", "If", "Else", "For", "While", "Break", "Continue", "Return", 
		"New", "Class", "This", "Identifier"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Mg.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MgParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MgParser.EOF, 0); }
		public List<DecContext> dec() {
			return getRuleContexts(DecContext.class);
		}
		public DecContext dec(int i) {
			return getRuleContext(DecContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Class) | (1L << Identifier))) != 0)) {
				{
				{
				setState(88);
				dec();
				}
				}
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(94);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DecContext extends ParserRuleContext {
		public VarDecContext varDec() {
			return getRuleContext(VarDecContext.class,0);
		}
		public FunctDecContext functDec() {
			return getRuleContext(FunctDecContext.class,0);
		}
		public ClassDecContext classDec() {
			return getRuleContext(ClassDecContext.class,0);
		}
		public DecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecContext dec() throws RecognitionException {
		DecContext _localctx = new DecContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_dec);
		try {
			setState(99);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				varDec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				functDec();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(98);
				classDec();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDecContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VarDeclaratorListContext varDeclaratorList() {
			return getRuleContext(VarDeclaratorListContext.class,0);
		}
		public VarDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterVarDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitVarDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitVarDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDecContext varDec() throws RecognitionException {
		VarDecContext _localctx = new VarDecContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_varDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			type();
			setState(102);
			varDeclaratorList();
			setState(103);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclaratorListContext extends ParserRuleContext {
		public List<VarDeclaratorContext> varDeclarator() {
			return getRuleContexts(VarDeclaratorContext.class);
		}
		public VarDeclaratorContext varDeclarator(int i) {
			return getRuleContext(VarDeclaratorContext.class,i);
		}
		public VarDeclaratorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclaratorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterVarDeclaratorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitVarDeclaratorList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitVarDeclaratorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclaratorListContext varDeclaratorList() throws RecognitionException {
		VarDeclaratorListContext _localctx = new VarDeclaratorListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_varDeclaratorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			varDeclarator();
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(106);
				match(T__1);
				setState(107);
				varDeclarator();
				}
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclaratorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public VarDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterVarDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitVarDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitVarDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclaratorContext varDeclarator() throws RecognitionException {
		VarDeclaratorContext _localctx = new VarDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_varDeclarator);
		try {
			setState(117);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(113);
				match(Identifier);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(114);
				match(Identifier);
				setState(115);
				match(T__2);
				setState(116);
				exp(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctDecContext extends ParserRuleContext {
		public Token functName;
		public SimpleTypeContext simpleType() {
			return getRuleContext(SimpleTypeContext.class,0);
		}
		public BlockStmContext blockStm() {
			return getRuleContext(BlockStmContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public FunctDecParaListContext functDecParaList() {
			return getRuleContext(FunctDecParaListContext.class,0);
		}
		public FunctDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterFunctDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitFunctDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitFunctDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctDecContext functDec() throws RecognitionException {
		FunctDecContext _localctx = new FunctDecContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_functDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			simpleType();
			setState(120);
			((FunctDecContext)_localctx).functName = match(Identifier);
			setState(121);
			match(T__3);
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				setState(122);
				functDecParaList();
				}
			}

			setState(125);
			match(T__4);
			setState(126);
			blockStm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctDecParaListContext extends ParserRuleContext {
		public List<FunctDecParaContext> functDecPara() {
			return getRuleContexts(FunctDecParaContext.class);
		}
		public FunctDecParaContext functDecPara(int i) {
			return getRuleContext(FunctDecParaContext.class,i);
		}
		public FunctDecParaListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functDecParaList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterFunctDecParaList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitFunctDecParaList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitFunctDecParaList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctDecParaListContext functDecParaList() throws RecognitionException {
		FunctDecParaListContext _localctx = new FunctDecParaListContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_functDecParaList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			functDecPara();
			setState(133);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(129);
				match(T__1);
				setState(130);
				functDecPara();
				}
				}
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctDecParaContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public FunctDecParaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functDecPara; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterFunctDecPara(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitFunctDecPara(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitFunctDecPara(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctDecParaContext functDecPara() throws RecognitionException {
		FunctDecParaContext _localctx = new FunctDecParaContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_functDecPara);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			type();
			setState(137);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDecContext extends ParserRuleContext {
		public Token className;
		public ClassBodyContext classBody() {
			return getRuleContext(ClassBodyContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public ClassDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterClassDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitClassDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitClassDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDecContext classDec() throws RecognitionException {
		ClassDecContext _localctx = new ClassDecContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_classDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(Class);
			setState(140);
			((ClassDecContext)_localctx).className = match(Identifier);
			setState(141);
			classBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyContext extends ParserRuleContext {
		public List<ClassBodyDecContext> classBodyDec() {
			return getRuleContexts(ClassBodyDecContext.class);
		}
		public ClassBodyDecContext classBodyDec(int i) {
			return getRuleContext(ClassBodyDecContext.class,i);
		}
		public ClassBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterClassBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitClassBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitClassBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBodyContext classBody() throws RecognitionException {
		ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_classBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(T__5);
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Class) | (1L << Identifier))) != 0)) {
				{
				{
				setState(144);
				classBodyDec();
				}
				}
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(150);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyDecContext extends ParserRuleContext {
		public ConstructorDecContext constructorDec() {
			return getRuleContext(ConstructorDecContext.class,0);
		}
		public FunctDecContext functDec() {
			return getRuleContext(FunctDecContext.class,0);
		}
		public VarDecContext varDec() {
			return getRuleContext(VarDecContext.class,0);
		}
		public ClassDecContext classDec() {
			return getRuleContext(ClassDecContext.class,0);
		}
		public ClassBodyDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBodyDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterClassBodyDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitClassBodyDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitClassBodyDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBodyDecContext classBodyDec() throws RecognitionException {
		ClassBodyDecContext _localctx = new ClassBodyDecContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_classBodyDec);
		try {
			setState(156);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				constructorDec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(153);
				functDec();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(154);
				varDec();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(155);
				classDec();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstructorDecContext extends ParserRuleContext {
		public Token className;
		public BlockStmContext blockStm() {
			return getRuleContext(BlockStmContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public FunctDecParaListContext functDecParaList() {
			return getRuleContext(FunctDecParaListContext.class,0);
		}
		public ConstructorDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterConstructorDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitConstructorDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitConstructorDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorDecContext constructorDec() throws RecognitionException {
		ConstructorDecContext _localctx = new ConstructorDecContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_constructorDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			((ConstructorDecContext)_localctx).className = match(Identifier);
			setState(159);
			match(T__3);
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				setState(160);
				functDecParaList();
				}
			}

			setState(163);
			match(T__4);
			setState(164);
			blockStm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StmContext extends ParserRuleContext {
		public VarDecStmContext varDecStm() {
			return getRuleContext(VarDecStmContext.class,0);
		}
		public IfStmContext ifStm() {
			return getRuleContext(IfStmContext.class,0);
		}
		public WhileStmContext whileStm() {
			return getRuleContext(WhileStmContext.class,0);
		}
		public ForStmContext forStm() {
			return getRuleContext(ForStmContext.class,0);
		}
		public BreakStmContext breakStm() {
			return getRuleContext(BreakStmContext.class,0);
		}
		public ContinueStmContext continueStm() {
			return getRuleContext(ContinueStmContext.class,0);
		}
		public ReturnStmContext returnStm() {
			return getRuleContext(ReturnStmContext.class,0);
		}
		public ExpStmContext expStm() {
			return getRuleContext(ExpStmContext.class,0);
		}
		public BlockStmContext blockStm() {
			return getRuleContext(BlockStmContext.class,0);
		}
		public EmptyStmContext emptyStm() {
			return getRuleContext(EmptyStmContext.class,0);
		}
		public StmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmContext stm() throws RecognitionException {
		StmContext _localctx = new StmContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_stm);
		try {
			setState(176);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				varDecStm();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(167);
				ifStm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(168);
				whileStm();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(169);
				forStm();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(170);
				breakStm();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(171);
				continueStm();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(172);
				returnStm();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(173);
				expStm();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(174);
				blockStm();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(175);
				emptyStm();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpStmContext extends ParserRuleContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ExpStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterExpStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitExpStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitExpStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpStmContext expStm() throws RecognitionException {
		ExpStmContext _localctx = new ExpStmContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_expStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			exp(0);
			setState(179);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockStmContext extends ParserRuleContext {
		public List<StmContext> stm() {
			return getRuleContexts(StmContext.class);
		}
		public StmContext stm(int i) {
			return getRuleContext(StmContext.class,i);
		}
		public BlockStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterBlockStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitBlockStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitBlockStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStmContext blockStm() throws RecognitionException {
		BlockStmContext _localctx = new BlockStmContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_blockStm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(T__5);
			setState(185);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__3) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Null) | (1L << If) | (1L << For) | (1L << While) | (1L << Break) | (1L << Continue) | (1L << Return) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				{
				setState(182);
				stm();
				}
				}
				setState(187);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(188);
			match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDecStmContext extends ParserRuleContext {
		public VarDecContext varDec() {
			return getRuleContext(VarDecContext.class,0);
		}
		public VarDecStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterVarDecStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitVarDecStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitVarDecStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDecStmContext varDecStm() throws RecognitionException {
		VarDecStmContext _localctx = new VarDecStmContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_varDecStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			varDec();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfStmContext extends ParserRuleContext {
		public StmContext then_;
		public StmContext else_;
		public TerminalNode If() { return getToken(MgParser.If, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public List<StmContext> stm() {
			return getRuleContexts(StmContext.class);
		}
		public StmContext stm(int i) {
			return getRuleContext(StmContext.class,i);
		}
		public TerminalNode Else() { return getToken(MgParser.Else, 0); }
		public IfStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterIfStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitIfStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitIfStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStmContext ifStm() throws RecognitionException {
		IfStmContext _localctx = new IfStmContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_ifStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			match(If);
			setState(193);
			match(T__3);
			setState(194);
			exp(0);
			setState(195);
			match(T__4);
			setState(196);
			((IfStmContext)_localctx).then_ = stm();
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(197);
				match(Else);
				setState(198);
				((IfStmContext)_localctx).else_ = stm();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhileStmContext extends ParserRuleContext {
		public TerminalNode While() { return getToken(MgParser.While, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public StmContext stm() {
			return getRuleContext(StmContext.class,0);
		}
		public WhileStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterWhileStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitWhileStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitWhileStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStmContext whileStm() throws RecognitionException {
		WhileStmContext _localctx = new WhileStmContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_whileStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(While);
			setState(202);
			match(T__3);
			setState(203);
			exp(0);
			setState(204);
			match(T__4);
			setState(205);
			stm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForStmContext extends ParserRuleContext {
		public TerminalNode For() { return getToken(MgParser.For, 0); }
		public ForControlContext forControl() {
			return getRuleContext(ForControlContext.class,0);
		}
		public StmContext stm() {
			return getRuleContext(StmContext.class,0);
		}
		public ForStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterForStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitForStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitForStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStmContext forStm() throws RecognitionException {
		ForStmContext _localctx = new ForStmContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_forStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(For);
			setState(208);
			forControl();
			setState(209);
			stm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForControlContext extends ParserRuleContext {
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ForUpdateContext forUpdate() {
			return getRuleContext(ForUpdateContext.class,0);
		}
		public ForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitForControl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitForControl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForControlContext forControl() throws RecognitionException {
		ForControlContext _localctx = new ForControlContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_forControl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			match(T__3);
			setState(213);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(212);
				forInit();
				}
				break;
			}
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(215);
				exp(0);
				}
			}

			setState(218);
			match(T__0);
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(219);
				forUpdate();
				}
			}

			setState(222);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForInitContext extends ParserRuleContext {
		public VarDecContext varDec() {
			return getRuleContext(VarDecContext.class,0);
		}
		public ExpListContext expList() {
			return getRuleContext(ExpListContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterForInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitForInit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitForInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_forInit);
		try {
			setState(228);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(224);
				varDec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(225);
				expList();
				setState(226);
				match(T__0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForUpdateContext extends ParserRuleContext {
		public ExpListContext expList() {
			return getRuleContext(ExpListContext.class,0);
		}
		public ForUpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forUpdate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterForUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitForUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitForUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForUpdateContext forUpdate() throws RecognitionException {
		ForUpdateContext _localctx = new ForUpdateContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_forUpdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			expList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BreakStmContext extends ParserRuleContext {
		public BreakStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterBreakStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitBreakStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitBreakStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStmContext breakStm() throws RecognitionException {
		BreakStmContext _localctx = new BreakStmContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_breakStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			match(Break);
			setState(233);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContinueStmContext extends ParserRuleContext {
		public ContinueStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterContinueStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitContinueStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitContinueStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStmContext continueStm() throws RecognitionException {
		ContinueStmContext _localctx = new ContinueStmContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_continueStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			match(Continue);
			setState(236);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnStmContext extends ParserRuleContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ReturnStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterReturnStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitReturnStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitReturnStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStmContext returnStm() throws RecognitionException {
		ReturnStmContext _localctx = new ReturnStmContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_returnStm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			match(Return);
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(239);
				exp(0);
				}
			}

			setState(242);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EmptyStmContext extends ParserRuleContext {
		public EmptyStmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyStm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterEmptyStm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitEmptyStm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitEmptyStm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyStmContext emptyStm() throws RecognitionException {
		EmptyStmContext _localctx = new EmptyStmContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_emptyStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpContext extends ParserRuleContext {
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
	 
		public ExpContext() { }
		public void copyFrom(ExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PrefixExpContext extends ExpContext {
		public Token op;
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public PrefixExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterPrefixExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitPrefixExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitPrefixExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignExpContext extends ExpContext {
		public ExpContext lhs;
		public ExpContext rhs;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public AssignExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterAssignExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitAssignExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitAssignExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArithBinaryExpContext extends ExpContext {
		public ExpContext lhs;
		public Token op;
		public ExpContext rhs;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public ArithBinaryExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArithBinaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArithBinaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArithBinaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SuffixExpContext extends ExpContext {
		public Token op;
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public SuffixExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterSuffixExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitSuffixExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitSuffixExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrimitiveExpContext extends ExpContext {
		public PrimaryExpContext primaryExp() {
			return getRuleContext(PrimaryExpContext.class,0);
		}
		public PrimitiveExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterPrimitiveExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitPrimitiveExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitPrimitiveExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullExpContext extends ExpContext {
		public TerminalNode Null() { return getToken(MgParser.Null, 0); }
		public NullExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterNullExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitNullExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitNullExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicBinaryExpContext extends ExpContext {
		public ExpContext lhs;
		public Token op;
		public ExpContext rhs;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public LogicBinaryExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterLogicBinaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitLogicBinaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitLogicBinaryExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		return exp(0);
	}

	private ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState);
		ExpContext _prevctx = _localctx;
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_exp, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
			case T__8:
				{
				_localctx = new PrefixExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(247);
				((PrefixExpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__7 || _la==T__8) ) {
					((PrefixExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(248);
				exp(14);
				}
				break;
			case T__9:
			case T__10:
				{
				_localctx = new PrefixExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(249);
				((PrefixExpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
					((PrefixExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(250);
				exp(13);
				}
				break;
			case T__11:
			case T__12:
				{
				_localctx = new PrefixExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(251);
				((PrefixExpContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__11 || _la==T__12) ) {
					((PrefixExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(252);
				exp(12);
				}
				break;
			case Null:
				{
				_localctx = new NullExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(253);
				match(Null);
				}
				break;
			case T__3:
			case LogicConstant:
			case PosIntegerConstant:
			case StringConstant:
			case New:
			case This:
			case Identifier:
				{
				_localctx = new PrimitiveExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(254);
				primaryExp(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(288);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(286);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(257);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(258);
						((ArithBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__13 || _la==T__14) ) {
							((ArithBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(259);
						((ArithBinaryExpContext)_localctx).rhs = exp(12);
						}
						break;
					case 2:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(260);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(261);
						((ArithBinaryExpContext)_localctx).op = match(T__15);
						setState(262);
						((ArithBinaryExpContext)_localctx).rhs = exp(11);
						}
						break;
					case 3:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(263);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(264);
						((ArithBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==T__10) ) {
							((ArithBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(265);
						((ArithBinaryExpContext)_localctx).rhs = exp(10);
						}
						break;
					case 4:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(266);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(267);
						((ArithBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__16 || _la==T__17) ) {
							((ArithBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(268);
						((ArithBinaryExpContext)_localctx).rhs = exp(9);
						}
						break;
					case 5:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(269);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(270);
						((ArithBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21))) != 0)) ) {
							((ArithBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(271);
						((ArithBinaryExpContext)_localctx).rhs = exp(8);
						}
						break;
					case 6:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(272);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(273);
						((ArithBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__22 || _la==T__23) ) {
							((ArithBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(274);
						((ArithBinaryExpContext)_localctx).rhs = exp(7);
						}
						break;
					case 7:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(275);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(276);
						((ArithBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__24) | (1L << T__25) | (1L << T__26))) != 0)) ) {
							((ArithBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(277);
						((ArithBinaryExpContext)_localctx).rhs = exp(6);
						}
						break;
					case 8:
						{
						_localctx = new LogicBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((LogicBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(278);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(279);
						((LogicBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__27 || _la==T__28) ) {
							((LogicBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(280);
						((LogicBinaryExpContext)_localctx).rhs = exp(5);
						}
						break;
					case 9:
						{
						_localctx = new AssignExpContext(new ExpContext(_parentctx, _parentState));
						((AssignExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(281);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(282);
						match(T__2);
						setState(283);
						((AssignExpContext)_localctx).rhs = exp(4);
						}
						break;
					case 10:
						{
						_localctx = new SuffixExpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(284);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(285);
						((SuffixExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__7 || _la==T__8) ) {
							((SuffixExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(290);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PrimaryExpContext extends ParserRuleContext {
		public PrimaryExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExp; }
	 
		public PrimaryExpContext() { }
		public void copyFrom(PrimaryExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MemberSelContext extends PrimaryExpContext {
		public Token memberName;
		public PrimaryExpContext primaryExp() {
			return getRuleContext(PrimaryExpContext.class,0);
		}
		public TerminalNode This() { return getToken(MgParser.This, 0); }
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public MemberSelContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterMemberSel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitMemberSel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitMemberSel(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VarPrimaryExpContext extends PrimaryExpContext {
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public VarPrimaryExpContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterVarPrimaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitVarPrimaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitVarPrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreatePrimaryExpContext extends PrimaryExpContext {
		public TerminalNode New() { return getToken(MgParser.New, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public CreatePrimaryExpContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterCreatePrimaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitCreatePrimaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitCreatePrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MethodSelContext extends PrimaryExpContext {
		public PrimaryExpContext primaryExp() {
			return getRuleContext(PrimaryExpContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public MethodSelContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterMethodSel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitMethodSel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitMethodSel(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralPrimaryExpContext extends PrimaryExpContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralPrimaryExpContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterLiteralPrimaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitLiteralPrimaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitLiteralPrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctCallExpContext extends PrimaryExpContext {
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public FunctCallExpContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterFunctCallExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitFunctCallExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitFunctCallExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ArrayAcsExpContext extends PrimaryExpContext {
		public PrimaryExpContext primaryExp() {
			return getRuleContext(PrimaryExpContext.class,0);
		}
		public ArrayAccessorContext arrayAccessor() {
			return getRuleContext(ArrayAccessorContext.class,0);
		}
		public ArrayAcsExpContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArrayAcsExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArrayAcsExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArrayAcsExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenPrimaryExpContext extends PrimaryExpContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ParenPrimaryExpContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterParenPrimaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitParenPrimaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitParenPrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SelfPrimaryExpContext extends PrimaryExpContext {
		public TerminalNode This() { return getToken(MgParser.This, 0); }
		public SelfPrimaryExpContext(PrimaryExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterSelfPrimaryExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitSelfPrimaryExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitSelfPrimaryExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpContext primaryExp() throws RecognitionException {
		return primaryExp(0);
	}

	private PrimaryExpContext primaryExp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PrimaryExpContext _localctx = new PrimaryExpContext(_ctx, _parentState);
		PrimaryExpContext _prevctx = _localctx;
		int _startState = 54;
		enterRecursionRule(_localctx, 54, RULE_primaryExp, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				_localctx = new ParenPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(292);
				match(T__3);
				setState(293);
				exp(0);
				setState(294);
				match(T__4);
				}
				break;
			case 2:
				{
				_localctx = new SelfPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(296);
				match(This);
				}
				break;
			case 3:
				{
				_localctx = new LiteralPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(297);
				literal();
				}
				break;
			case 4:
				{
				_localctx = new CreatePrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(298);
				match(New);
				setState(299);
				creator();
				}
				break;
			case 5:
				{
				_localctx = new VarPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(300);
				match(Identifier);
				}
				break;
			case 6:
				{
				_localctx = new FunctCallExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(301);
				match(Identifier);
				setState(302);
				arguments();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(324);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(322);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new MemberSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(305);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(306);
						match(T__29);
						setState(307);
						((MemberSelContext)_localctx).memberName = match(This);
						}
						break;
					case 2:
						{
						_localctx = new MemberSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(308);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(309);
						match(T__29);
						setState(310);
						((MemberSelContext)_localctx).memberName = match(Identifier);
						}
						break;
					case 3:
						{
						_localctx = new MemberSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(311);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(312);
						match(T__29);
						setState(313);
						match(T__3);
						setState(314);
						((MemberSelContext)_localctx).memberName = match(Identifier);
						setState(315);
						match(T__4);
						}
						break;
					case 4:
						{
						_localctx = new MethodSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(316);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(317);
						match(T__29);
						setState(318);
						match(Identifier);
						setState(319);
						arguments();
						}
						break;
					case 5:
						{
						_localctx = new ArrayAcsExpContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(320);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(321);
						arrayAccessor();
						}
						break;
					}
					} 
				}
				setState(326);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public IntegerLiteralContext integerLiteral() {
			return getRuleContext(IntegerLiteralContext.class,0);
		}
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public LogicLiteralContext logicLiteral() {
			return getRuleContext(LogicLiteralContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_literal);
		try {
			setState(330);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PosIntegerConstant:
				enterOuterAlt(_localctx, 1);
				{
				setState(327);
				integerLiteral();
				}
				break;
			case StringConstant:
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				stringLiteral();
				}
				break;
			case LogicConstant:
				enterOuterAlt(_localctx, 3);
				{
				setState(329);
				logicLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayAccessorContext extends ParserRuleContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ArrayAccessorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayAccessor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArrayAccessor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArrayAccessor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArrayAccessor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayAccessorContext arrayAccessor() throws RecognitionException {
		ArrayAccessorContext _localctx = new ArrayAccessorContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_arrayAccessor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(T__30);
			setState(333);
			exp(0);
			setState(334);
			match(T__31);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public ExpListContext expList() {
			return getRuleContext(ExpListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			match(T__3);
			setState(338);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(337);
				expList();
				}
			}

			setState(340);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpListContext extends ParserRuleContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public ExpListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterExpList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitExpList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitExpList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpListContext expList() throws RecognitionException {
		ExpListContext _localctx = new ExpListContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_expList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(342);
			exp(0);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(343);
				match(T__1);
				setState(344);
				exp(0);
				}
				}
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatorContext extends ParserRuleContext {
		public SimpleTypeContext simpleType() {
			return getRuleContext(SimpleTypeContext.class,0);
		}
		public List<ArrayCreatorDimContext> arrayCreatorDim() {
			return getRuleContexts(ArrayCreatorDimContext.class);
		}
		public ArrayCreatorDimContext arrayCreatorDim(int i) {
			return getRuleContext(ArrayCreatorDimContext.class,i);
		}
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterCreator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitCreator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_creator);
		try {
			int _alt;
			setState(361);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(350);
				simpleType();
				setState(354);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(351);
						arrayCreatorDim();
						}
						} 
					}
					setState(356);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(357);
				simpleType();
				setState(358);
				match(T__3);
				setState(359);
				match(T__4);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayCreatorDimContext extends ParserRuleContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ArrayCreatorDimContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreatorDim; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArrayCreatorDim(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArrayCreatorDim(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArrayCreatorDim(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayCreatorDimContext arrayCreatorDim() throws RecognitionException {
		ArrayCreatorDimContext _localctx = new ArrayCreatorDimContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_arrayCreatorDim);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(T__30);
			setState(365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(364);
				exp(0);
				}
			}

			setState(367);
			match(T__31);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerLiteralContext extends ParserRuleContext {
		public TerminalNode PosIntegerConstant() { return getToken(MgParser.PosIntegerConstant, 0); }
		public IntegerLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterIntegerLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitIntegerLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitIntegerLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerLiteralContext integerLiteral() throws RecognitionException {
		IntegerLiteralContext _localctx = new IntegerLiteralContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_integerLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			match(PosIntegerConstant);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode StringConstant() { return getToken(MgParser.StringConstant, 0); }
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			match(StringConstant);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicLiteralContext extends ParserRuleContext {
		public TerminalNode LogicConstant() { return getToken(MgParser.LogicConstant, 0); }
		public LogicLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterLogicLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitLogicLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitLogicLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicLiteralContext logicLiteral() throws RecognitionException {
		LogicLiteralContext _localctx = new LogicLiteralContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_logicLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(373);
			match(LogicConstant);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public SimpleTypeContext simpleType() {
			return getRuleContext(SimpleTypeContext.class,0);
		}
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_type);
		try {
			setState(377);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(375);
				simpleType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(376);
				arrayType();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public UserTypeContext userType() {
			return getRuleContext(UserTypeContext.class,0);
		}
		public SimpleTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterSimpleType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitSimpleType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitSimpleType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeContext simpleType() throws RecognitionException {
		SimpleTypeContext _localctx = new SimpleTypeContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_simpleType);
		try {
			setState(381);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
			case Void:
				enterOuterAlt(_localctx, 1);
				{
				setState(379);
				primitiveType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(380);
				userType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode Bool() { return getToken(MgParser.Bool, 0); }
		public TerminalNode Int() { return getToken(MgParser.Int, 0); }
		public TerminalNode String() { return getToken(MgParser.String, 0); }
		public TerminalNode Void() { return getToken(MgParser.Void, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitPrimitiveType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UserTypeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MgParser.Identifier, 0); }
		public UserTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterUserType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitUserType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitUserType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UserTypeContext userType() throws RecognitionException {
		UserTypeContext _localctx = new UserTypeContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_userType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayTypeContext extends ParserRuleContext {
		public SimpleTypeContext simpleType() {
			return getRuleContext(SimpleTypeContext.class,0);
		}
		public ArrayDimDecListContext arrayDimDecList() {
			return getRuleContext(ArrayDimDecListContext.class,0);
		}
		public ArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArrayType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			simpleType();
			setState(388);
			arrayDimDecList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayDimDecListContext extends ParserRuleContext {
		public List<ArrayDimDecContext> arrayDimDec() {
			return getRuleContexts(ArrayDimDecContext.class);
		}
		public ArrayDimDecContext arrayDimDec(int i) {
			return getRuleContext(ArrayDimDecContext.class,i);
		}
		public ArrayDimDecListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayDimDecList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArrayDimDecList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArrayDimDecList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArrayDimDecList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayDimDecListContext arrayDimDecList() throws RecognitionException {
		ArrayDimDecListContext _localctx = new ArrayDimDecListContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_arrayDimDecList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(390);
				arrayDimDec();
				}
				}
				setState(393); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__30 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayDimDecContext extends ParserRuleContext {
		public ArrayDimDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayDimDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterArrayDimDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitArrayDimDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitArrayDimDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayDimDecContext arrayDimDec() throws RecognitionException {
		ArrayDimDecContext _localctx = new ArrayDimDecContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_arrayDimDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			match(T__30);
			setState(396);
			match(T__31);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 26:
			return exp_sempred((ExpContext)_localctx, predIndex);
		case 27:
			return primaryExp_sempred((PrimaryExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 11);
		case 1:
			return precpred(_ctx, 10);
		case 2:
			return precpred(_ctx, 9);
		case 3:
			return precpred(_ctx, 8);
		case 4:
			return precpred(_ctx, 7);
		case 5:
			return precpred(_ctx, 6);
		case 6:
			return precpred(_ctx, 5);
		case 7:
			return precpred(_ctx, 4);
		case 8:
			return precpred(_ctx, 3);
		case 9:
			return precpred(_ctx, 15);
		}
		return true;
	}
	private boolean primaryExp_sempred(PrimaryExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return precpred(_ctx, 6);
		case 11:
			return precpred(_ctx, 5);
		case 12:
			return precpred(_ctx, 4);
		case 13:
			return precpred(_ctx, 3);
		case 14:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3;\u0191\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\3\2\7\2\\\n\2\f\2\16\2_\13\2\3\2\3\2\3\3\3\3\3\3\5\3f\n\3\3"+
		"\4\3\4\3\4\3\4\3\5\3\5\3\5\7\5o\n\5\f\5\16\5r\13\5\3\6\3\6\3\6\3\6\5\6"+
		"x\n\6\3\7\3\7\3\7\3\7\5\7~\n\7\3\7\3\7\3\7\3\b\3\b\3\b\7\b\u0086\n\b\f"+
		"\b\16\b\u0089\13\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\7\13\u0094\n"+
		"\13\f\13\16\13\u0097\13\13\3\13\3\13\3\f\3\f\3\f\3\f\5\f\u009f\n\f\3\r"+
		"\3\r\3\r\5\r\u00a4\n\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\5\16\u00b3\n\16\3\17\3\17\3\17\3\20\3\20\7\20\u00ba\n"+
		"\20\f\20\16\20\u00bd\13\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\5\22\u00ca\n\22\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\5\25\u00d8\n\25\3\25\5\25\u00db\n\25\3\25\3\25\5\25\u00df"+
		"\n\25\3\25\3\25\3\26\3\26\3\26\3\26\5\26\u00e7\n\26\3\27\3\27\3\30\3\30"+
		"\3\30\3\31\3\31\3\31\3\32\3\32\5\32\u00f3\n\32\3\32\3\32\3\33\3\33\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u0102\n\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\7\34\u0121"+
		"\n\34\f\34\16\34\u0124\13\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3"+
		"\35\3\35\3\35\3\35\5\35\u0132\n\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\7\35\u0145\n\35\f\35"+
		"\16\35\u0148\13\35\3\36\3\36\3\36\5\36\u014d\n\36\3\37\3\37\3\37\3\37"+
		"\3 \3 \5 \u0155\n \3 \3 \3!\3!\3!\7!\u015c\n!\f!\16!\u015f\13!\3\"\3\""+
		"\7\"\u0163\n\"\f\"\16\"\u0166\13\"\3\"\3\"\3\"\3\"\5\"\u016c\n\"\3#\3"+
		"#\5#\u0170\n#\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\5\'\u017c\n\'\3(\3(\5(\u0180"+
		"\n(\3)\3)\3*\3*\3+\3+\3+\3,\6,\u018a\n,\r,\16,\u018b\3-\3-\3-\3-\2\4\66"+
		"8.\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BD"+
		"FHJLNPRTVX\2\f\3\2\n\13\3\2\f\r\3\2\16\17\3\2\20\21\3\2\23\24\3\2\25\30"+
		"\3\2\31\32\3\2\33\35\3\2\36\37\3\2*-\2\u01a2\2]\3\2\2\2\4e\3\2\2\2\6g"+
		"\3\2\2\2\bk\3\2\2\2\nw\3\2\2\2\fy\3\2\2\2\16\u0082\3\2\2\2\20\u008a\3"+
		"\2\2\2\22\u008d\3\2\2\2\24\u0091\3\2\2\2\26\u009e\3\2\2\2\30\u00a0\3\2"+
		"\2\2\32\u00b2\3\2\2\2\34\u00b4\3\2\2\2\36\u00b7\3\2\2\2 \u00c0\3\2\2\2"+
		"\"\u00c2\3\2\2\2$\u00cb\3\2\2\2&\u00d1\3\2\2\2(\u00d5\3\2\2\2*\u00e6\3"+
		"\2\2\2,\u00e8\3\2\2\2.\u00ea\3\2\2\2\60\u00ed\3\2\2\2\62\u00f0\3\2\2\2"+
		"\64\u00f6\3\2\2\2\66\u0101\3\2\2\28\u0131\3\2\2\2:\u014c\3\2\2\2<\u014e"+
		"\3\2\2\2>\u0152\3\2\2\2@\u0158\3\2\2\2B\u016b\3\2\2\2D\u016d\3\2\2\2F"+
		"\u0173\3\2\2\2H\u0175\3\2\2\2J\u0177\3\2\2\2L\u017b\3\2\2\2N\u017f\3\2"+
		"\2\2P\u0181\3\2\2\2R\u0183\3\2\2\2T\u0185\3\2\2\2V\u0189\3\2\2\2X\u018d"+
		"\3\2\2\2Z\\\5\4\3\2[Z\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^`\3\2\2\2"+
		"_]\3\2\2\2`a\7\2\2\3a\3\3\2\2\2bf\5\6\4\2cf\5\f\7\2df\5\22\n\2eb\3\2\2"+
		"\2ec\3\2\2\2ed\3\2\2\2f\5\3\2\2\2gh\5L\'\2hi\5\b\5\2ij\7\3\2\2j\7\3\2"+
		"\2\2kp\5\n\6\2lm\7\4\2\2mo\5\n\6\2nl\3\2\2\2or\3\2\2\2pn\3\2\2\2pq\3\2"+
		"\2\2q\t\3\2\2\2rp\3\2\2\2sx\7;\2\2tu\7;\2\2uv\7\5\2\2vx\5\66\34\2ws\3"+
		"\2\2\2wt\3\2\2\2x\13\3\2\2\2yz\5N(\2z{\7;\2\2{}\7\6\2\2|~\5\16\b\2}|\3"+
		"\2\2\2}~\3\2\2\2~\177\3\2\2\2\177\u0080\7\7\2\2\u0080\u0081\5\36\20\2"+
		"\u0081\r\3\2\2\2\u0082\u0087\5\20\t\2\u0083\u0084\7\4\2\2\u0084\u0086"+
		"\5\20\t\2\u0085\u0083\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085\3\2\2\2"+
		"\u0087\u0088\3\2\2\2\u0088\17\3\2\2\2\u0089\u0087\3\2\2\2\u008a\u008b"+
		"\5L\'\2\u008b\u008c\7;\2\2\u008c\21\3\2\2\2\u008d\u008e\79\2\2\u008e\u008f"+
		"\7;\2\2\u008f\u0090\5\24\13\2\u0090\23\3\2\2\2\u0091\u0095\7\b\2\2\u0092"+
		"\u0094\5\26\f\2\u0093\u0092\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3"+
		"\2\2\2\u0095\u0096\3\2\2\2\u0096\u0098\3\2\2\2\u0097\u0095\3\2\2\2\u0098"+
		"\u0099\7\t\2\2\u0099\25\3\2\2\2\u009a\u009f\5\30\r\2\u009b\u009f\5\f\7"+
		"\2\u009c\u009f\5\6\4\2\u009d\u009f\5\22\n\2\u009e\u009a\3\2\2\2\u009e"+
		"\u009b\3\2\2\2\u009e\u009c\3\2\2\2\u009e\u009d\3\2\2\2\u009f\27\3\2\2"+
		"\2\u00a0\u00a1\7;\2\2\u00a1\u00a3\7\6\2\2\u00a2\u00a4\5\16\b\2\u00a3\u00a2"+
		"\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a6\7\7\2\2\u00a6"+
		"\u00a7\5\36\20\2\u00a7\31\3\2\2\2\u00a8\u00b3\5 \21\2\u00a9\u00b3\5\""+
		"\22\2\u00aa\u00b3\5$\23\2\u00ab\u00b3\5&\24\2\u00ac\u00b3\5.\30\2\u00ad"+
		"\u00b3\5\60\31\2\u00ae\u00b3\5\62\32\2\u00af\u00b3\5\34\17\2\u00b0\u00b3"+
		"\5\36\20\2\u00b1\u00b3\5\64\33\2\u00b2\u00a8\3\2\2\2\u00b2\u00a9\3\2\2"+
		"\2\u00b2\u00aa\3\2\2\2\u00b2\u00ab\3\2\2\2\u00b2\u00ac\3\2\2\2\u00b2\u00ad"+
		"\3\2\2\2\u00b2\u00ae\3\2\2\2\u00b2\u00af\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2"+
		"\u00b1\3\2\2\2\u00b3\33\3\2\2\2\u00b4\u00b5\5\66\34\2\u00b5\u00b6\7\3"+
		"\2\2\u00b6\35\3\2\2\2\u00b7\u00bb\7\b\2\2\u00b8\u00ba\5\32\16\2\u00b9"+
		"\u00b8\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb\u00b9\3\2\2\2\u00bb\u00bc\3\2"+
		"\2\2\u00bc\u00be\3\2\2\2\u00bd\u00bb\3\2\2\2\u00be\u00bf\7\t\2\2\u00bf"+
		"\37\3\2\2\2\u00c0\u00c1\5\6\4\2\u00c1!\3\2\2\2\u00c2\u00c3\7\61\2\2\u00c3"+
		"\u00c4\7\6\2\2\u00c4\u00c5\5\66\34\2\u00c5\u00c6\7\7\2\2\u00c6\u00c9\5"+
		"\32\16\2\u00c7\u00c8\7\62\2\2\u00c8\u00ca\5\32\16\2\u00c9\u00c7\3\2\2"+
		"\2\u00c9\u00ca\3\2\2\2\u00ca#\3\2\2\2\u00cb\u00cc\7\64\2\2\u00cc\u00cd"+
		"\7\6\2\2\u00cd\u00ce\5\66\34\2\u00ce\u00cf\7\7\2\2\u00cf\u00d0\5\32\16"+
		"\2\u00d0%\3\2\2\2\u00d1\u00d2\7\63\2\2\u00d2\u00d3\5(\25\2\u00d3\u00d4"+
		"\5\32\16\2\u00d4\'\3\2\2\2\u00d5\u00d7\7\6\2\2\u00d6\u00d8\5*\26\2\u00d7"+
		"\u00d6\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00da\3\2\2\2\u00d9\u00db\5\66"+
		"\34\2\u00da\u00d9\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc"+
		"\u00de\7\3\2\2\u00dd\u00df\5,\27\2\u00de\u00dd\3\2\2\2\u00de\u00df\3\2"+
		"\2\2\u00df\u00e0\3\2\2\2\u00e0\u00e1\7\7\2\2\u00e1)\3\2\2\2\u00e2\u00e7"+
		"\5\6\4\2\u00e3\u00e4\5@!\2\u00e4\u00e5\7\3\2\2\u00e5\u00e7\3\2\2\2\u00e6"+
		"\u00e2\3\2\2\2\u00e6\u00e3\3\2\2\2\u00e7+\3\2\2\2\u00e8\u00e9\5@!\2\u00e9"+
		"-\3\2\2\2\u00ea\u00eb\7\65\2\2\u00eb\u00ec\7\3\2\2\u00ec/\3\2\2\2\u00ed"+
		"\u00ee\7\66\2\2\u00ee\u00ef\7\3\2\2\u00ef\61\3\2\2\2\u00f0\u00f2\7\67"+
		"\2\2\u00f1\u00f3\5\66\34\2\u00f2\u00f1\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3"+
		"\u00f4\3\2\2\2\u00f4\u00f5\7\3\2\2\u00f5\63\3\2\2\2\u00f6\u00f7\7\3\2"+
		"\2\u00f7\65\3\2\2\2\u00f8\u00f9\b\34\1\2\u00f9\u00fa\t\2\2\2\u00fa\u0102"+
		"\5\66\34\20\u00fb\u00fc\t\3\2\2\u00fc\u0102\5\66\34\17\u00fd\u00fe\t\4"+
		"\2\2\u00fe\u0102\5\66\34\16\u00ff\u0102\7.\2\2\u0100\u0102\58\35\2\u0101"+
		"\u00f8\3\2\2\2\u0101\u00fb\3\2\2\2\u0101\u00fd\3\2\2\2\u0101\u00ff\3\2"+
		"\2\2\u0101\u0100\3\2\2\2\u0102\u0122\3\2\2\2\u0103\u0104\f\r\2\2\u0104"+
		"\u0105\t\5\2\2\u0105\u0121\5\66\34\16\u0106\u0107\f\f\2\2\u0107\u0108"+
		"\7\22\2\2\u0108\u0121\5\66\34\r\u0109\u010a\f\13\2\2\u010a\u010b\t\3\2"+
		"\2\u010b\u0121\5\66\34\f\u010c\u010d\f\n\2\2\u010d\u010e\t\6\2\2\u010e"+
		"\u0121\5\66\34\13\u010f\u0110\f\t\2\2\u0110\u0111\t\7\2\2\u0111\u0121"+
		"\5\66\34\n\u0112\u0113\f\b\2\2\u0113\u0114\t\b\2\2\u0114\u0121\5\66\34"+
		"\t\u0115\u0116\f\7\2\2\u0116\u0117\t\t\2\2\u0117\u0121\5\66\34\b\u0118"+
		"\u0119\f\6\2\2\u0119\u011a\t\n\2\2\u011a\u0121\5\66\34\7\u011b\u011c\f"+
		"\5\2\2\u011c\u011d\7\5\2\2\u011d\u0121\5\66\34\6\u011e\u011f\f\21\2\2"+
		"\u011f\u0121\t\2\2\2\u0120\u0103\3\2\2\2\u0120\u0106\3\2\2\2\u0120\u0109"+
		"\3\2\2\2\u0120\u010c\3\2\2\2\u0120\u010f\3\2\2\2\u0120\u0112\3\2\2\2\u0120"+
		"\u0115\3\2\2\2\u0120\u0118\3\2\2\2\u0120\u011b\3\2\2\2\u0120\u011e\3\2"+
		"\2\2\u0121\u0124\3\2\2\2\u0122\u0120\3\2\2\2\u0122\u0123\3\2\2\2\u0123"+
		"\67\3\2\2\2\u0124\u0122\3\2\2\2\u0125\u0126\b\35\1\2\u0126\u0127\7\6\2"+
		"\2\u0127\u0128\5\66\34\2\u0128\u0129\7\7\2\2\u0129\u0132\3\2\2\2\u012a"+
		"\u0132\7:\2\2\u012b\u0132\5:\36\2\u012c\u012d\78\2\2\u012d\u0132\5B\""+
		"\2\u012e\u0132\7;\2\2\u012f\u0130\7;\2\2\u0130\u0132\5> \2\u0131\u0125"+
		"\3\2\2\2\u0131\u012a\3\2\2\2\u0131\u012b\3\2\2\2\u0131\u012c\3\2\2\2\u0131"+
		"\u012e\3\2\2\2\u0131\u012f\3\2\2\2\u0132\u0146\3\2\2\2\u0133\u0134\f\b"+
		"\2\2\u0134\u0135\7 \2\2\u0135\u0145\7:\2\2\u0136\u0137\f\7\2\2\u0137\u0138"+
		"\7 \2\2\u0138\u0145\7;\2\2\u0139\u013a\f\6\2\2\u013a\u013b\7 \2\2\u013b"+
		"\u013c\7\6\2\2\u013c\u013d\7;\2\2\u013d\u0145\7\7\2\2\u013e\u013f\f\5"+
		"\2\2\u013f\u0140\7 \2\2\u0140\u0141\7;\2\2\u0141\u0145\5> \2\u0142\u0143"+
		"\f\4\2\2\u0143\u0145\5<\37\2\u0144\u0133\3\2\2\2\u0144\u0136\3\2\2\2\u0144"+
		"\u0139\3\2\2\2\u0144\u013e\3\2\2\2\u0144\u0142\3\2\2\2\u0145\u0148\3\2"+
		"\2\2\u0146\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u01479\3\2\2\2\u0148\u0146"+
		"\3\2\2\2\u0149\u014d\5F$\2\u014a\u014d\5H%\2\u014b\u014d\5J&\2\u014c\u0149"+
		"\3\2\2\2\u014c\u014a\3\2\2\2\u014c\u014b\3\2\2\2\u014d;\3\2\2\2\u014e"+
		"\u014f\7!\2\2\u014f\u0150\5\66\34\2\u0150\u0151\7\"\2\2\u0151=\3\2\2\2"+
		"\u0152\u0154\7\6\2\2\u0153\u0155\5@!\2\u0154\u0153\3\2\2\2\u0154\u0155"+
		"\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u0157\7\7\2\2\u0157?\3\2\2\2\u0158"+
		"\u015d\5\66\34\2\u0159\u015a\7\4\2\2\u015a\u015c\5\66\34\2\u015b\u0159"+
		"\3\2\2\2\u015c\u015f\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015e"+
		"A\3\2\2\2\u015f\u015d\3\2\2\2\u0160\u0164\5N(\2\u0161\u0163\5D#\2\u0162"+
		"\u0161\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162\3\2\2\2\u0164\u0165\3\2"+
		"\2\2\u0165\u016c\3\2\2\2\u0166\u0164\3\2\2\2\u0167\u0168\5N(\2\u0168\u0169"+
		"\7\6\2\2\u0169\u016a\7\7\2\2\u016a\u016c\3\2\2\2\u016b\u0160\3\2\2\2\u016b"+
		"\u0167\3\2\2\2\u016cC\3\2\2\2\u016d\u016f\7!\2\2\u016e\u0170\5\66\34\2"+
		"\u016f\u016e\3\2\2\2\u016f\u0170\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0172"+
		"\7\"\2\2\u0172E\3\2\2\2\u0173\u0174\7$\2\2\u0174G\3\2\2\2\u0175\u0176"+
		"\7%\2\2\u0176I\3\2\2\2\u0177\u0178\7#\2\2\u0178K\3\2\2\2\u0179\u017c\5"+
		"N(\2\u017a\u017c\5T+\2\u017b\u0179\3\2\2\2\u017b\u017a\3\2\2\2\u017cM"+
		"\3\2\2\2\u017d\u0180\5P)\2\u017e\u0180\5R*\2\u017f\u017d\3\2\2\2\u017f"+
		"\u017e\3\2\2\2\u0180O\3\2\2\2\u0181\u0182\t\13\2\2\u0182Q\3\2\2\2\u0183"+
		"\u0184\7;\2\2\u0184S\3\2\2\2\u0185\u0186\5N(\2\u0186\u0187\5V,\2\u0187"+
		"U\3\2\2\2\u0188\u018a\5X-\2\u0189\u0188\3\2\2\2\u018a\u018b\3\2\2\2\u018b"+
		"\u0189\3\2\2\2\u018b\u018c\3\2\2\2\u018cW\3\2\2\2\u018d\u018e\7!\2\2\u018e"+
		"\u018f\7\"\2\2\u018fY\3\2\2\2\"]epw}\u0087\u0095\u009e\u00a3\u00b2\u00bb"+
		"\u00c9\u00d7\u00da\u00de\u00e6\u00f2\u0101\u0120\u0122\u0131\u0144\u0146"+
		"\u014c\u0154\u015d\u0164\u016b\u016f\u017b\u017f\u018b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}