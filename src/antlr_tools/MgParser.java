package antlr_tools;// Generated from Mg.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MgParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

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
		RULE_prog = 0, RULE_dec = 1, RULE_globalDec = 2, RULE_functDec = 3, RULE_classDec = 4, 
		RULE_varDec = 5, RULE_varDeclaratorList = 6, RULE_varDeclarator = 7, RULE_functDecParaList = 8, 
		RULE_functDecPara = 9, RULE_classBody = 10, RULE_classBodyDec = 11, RULE_fieldDec = 12, 
		RULE_methodDec = 13, RULE_constructorDec = 14, RULE_stm = 15, RULE_expStm = 16, 
		RULE_blockStm = 17, RULE_varDecStm = 18, RULE_ifStm = 19, RULE_whileStm = 20, 
		RULE_forStm = 21, RULE_forControl = 22, RULE_forInit = 23, RULE_forUpdate = 24, 
		RULE_breakStm = 25, RULE_continueStm = 26, RULE_returnStm = 27, RULE_emptyStm = 28, 
		RULE_exp = 29, RULE_primaryExp = 30, RULE_literal = 31, RULE_arrayAccessor = 32, 
		RULE_arguments = 33, RULE_expList = 34, RULE_creator = 35, RULE_arrayCreatorDim = 36, 
		RULE_integerLiteral = 37, RULE_stringLiteral = 38, RULE_logicLiteral = 39, 
		RULE_type = 40, RULE_simpleType = 41, RULE_primitiveType = 42, RULE_userType = 43, 
		RULE_arrayType = 44, RULE_arrayDimDecList = 45, RULE_arrayDimDec = 46;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "dec", "globalDec", "functDec", "classDec", "varDec", "varDeclaratorList", 
			"varDeclarator", "functDecParaList", "functDecPara", "classBody", "classBodyDec", 
			"fieldDec", "methodDec", "constructorDec", "stm", "expStm", "blockStm", 
			"varDecStm", "ifStm", "whileStm", "forStm", "forControl", "forInit", 
			"forUpdate", "breakStm", "continueStm", "returnStm", "emptyStm", "exp", 
			"primaryExp", "literal", "arrayAccessor", "arguments", "expList", "creator", 
			"arrayCreatorDim", "integerLiteral", "stringLiteral", "logicLiteral", 
			"type", "simpleType", "primitiveType", "userType", "arrayType", "arrayDimDecList", 
			"arrayDimDec"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "')'", "','", "'='", "'{'", "'}'", "'++'", "'--'", 
			"'+'", "'-'", "'!'", "'~'", "'*'", "'/'", "'%'", "'<<'", "'>>'", "'>'", 
			"'>='", "'<'", "'<='", "'=='", "'!='", "'&'", "'^'", "'|'", "'&&'", "'||'", 
			"'.'", "'['", "']'", null, null, null, null, null, null, null, "'bool'", 
			"'int'", "'string'", "'void'", "'null'", "'true'", "'false'", "'if'", 
			"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", 
			"'class'", "'this'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "LogicConstant", 
			"PosIntegerConstant", "StringConstant", "LineComment", "BlockComment", 
			"WhiteSpace", "NewLine", "Bool", "Int", "String", "Void", "Null", "True", 
			"False", "If", "Else", "For", "While", "Break", "Continue", "Return", 
			"New", "Class", "This", "Identifier"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Class) | (1L << Identifier))) != 0)) {
				{
				{
				setState(94);
				dec();
				}
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(100);
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
		public GlobalDecContext globalDec() {
			return getRuleContext(GlobalDecContext.class,0);
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
			setState(107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(102);
				globalDec();
				setState(103);
				match(T__0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(105);
				functDec();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(106);
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

	public static class GlobalDecContext extends ParserRuleContext {
		public VarDecContext varDec() {
			return getRuleContext(VarDecContext.class,0);
		}
		public GlobalDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globalDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterGlobalDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitGlobalDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitGlobalDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalDecContext globalDec() throws RecognitionException {
		GlobalDecContext _localctx = new GlobalDecContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_globalDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
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

	public static class FunctDecContext extends ParserRuleContext {
		public Token functName;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
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
		enterRule(_localctx, 6, RULE_functDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			type();
			setState(112);
			((FunctDecContext)_localctx).functName = match(Identifier);
			setState(113);
			match(T__1);
			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				setState(114);
				functDecParaList();
				}
			}

			setState(117);
			match(T__2);
			setState(118);
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

	public static class ClassDecContext extends ParserRuleContext {
		public Token className;
		public TerminalNode Class() { return getToken(MgParser.Class, 0); }
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
		enterRule(_localctx, 8, RULE_classDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(Class);
			setState(121);
			((ClassDecContext)_localctx).className = match(Identifier);
			setState(122);
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
		enterRule(_localctx, 10, RULE_varDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			type();
			setState(125);
			varDeclaratorList();
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
		enterRule(_localctx, 12, RULE_varDeclaratorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			varDeclarator();
			setState(132);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(128);
				match(T__3);
				setState(129);
				varDeclarator();
				}
				}
				setState(134);
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
		enterRule(_localctx, 14, RULE_varDeclarator);
		try {
			setState(139);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(135);
				match(Identifier);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(136);
				match(Identifier);
				setState(137);
				match(T__4);
				setState(138);
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
		enterRule(_localctx, 16, RULE_functDecParaList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			functDecPara();
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(142);
				match(T__3);
				setState(143);
				functDecPara();
				}
				}
				setState(148);
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
		enterRule(_localctx, 18, RULE_functDecPara);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			type();
			setState(150);
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
		enterRule(_localctx, 20, RULE_classBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(T__5);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				{
				setState(153);
				classBodyDec();
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(159);
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
		public MethodDecContext methodDec() {
			return getRuleContext(MethodDecContext.class,0);
		}
		public FieldDecContext fieldDec() {
			return getRuleContext(FieldDecContext.class,0);
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
		enterRule(_localctx, 22, RULE_classBodyDec);
		try {
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(161);
				constructorDec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(162);
				methodDec();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(163);
				fieldDec();
				setState(164);
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

	public static class FieldDecContext extends ParserRuleContext {
		public VarDecContext varDec() {
			return getRuleContext(VarDecContext.class,0);
		}
		public FieldDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterFieldDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitFieldDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitFieldDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldDecContext fieldDec() throws RecognitionException {
		FieldDecContext _localctx = new FieldDecContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_fieldDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
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

	public static class MethodDecContext extends ParserRuleContext {
		public FunctDecContext functDec() {
			return getRuleContext(FunctDecContext.class,0);
		}
		public MethodDecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodDec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).enterMethodDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MgListener ) ((MgListener)listener).exitMethodDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MgVisitor ) return ((MgVisitor<? extends T>)visitor).visitMethodDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodDecContext methodDec() throws RecognitionException {
		MethodDecContext _localctx = new MethodDecContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_methodDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			functDec();
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
		enterRule(_localctx, 28, RULE_constructorDec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			((ConstructorDecContext)_localctx).className = match(Identifier);
			setState(173);
			match(T__1);
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Identifier))) != 0)) {
				{
				setState(174);
				functDecParaList();
				}
			}

			setState(177);
			match(T__2);
			setState(178);
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
		enterRule(_localctx, 30, RULE_stm);
		try {
			setState(190);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(180);
				varDecStm();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(181);
				ifStm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(182);
				whileStm();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(183);
				forStm();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(184);
				breakStm();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(185);
				continueStm();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(186);
				returnStm();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(187);
				expStm();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(188);
				blockStm();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(189);
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
		enterRule(_localctx, 32, RULE_expStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			exp(0);
			setState(193);
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
		enterRule(_localctx, 34, RULE_blockStm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(T__5);
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Null) | (1L << If) | (1L << For) | (1L << While) | (1L << Break) | (1L << Continue) | (1L << Return) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				{
				setState(196);
				stm();
				}
				}
				setState(201);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(202);
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
		enterRule(_localctx, 36, RULE_varDecStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			varDec();
			setState(205);
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
		enterRule(_localctx, 38, RULE_ifStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(If);
			setState(208);
			match(T__1);
			setState(209);
			exp(0);
			setState(210);
			match(T__2);
			setState(211);
			((IfStmContext)_localctx).then_ = stm();
			setState(214);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(212);
				match(Else);
				setState(213);
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
		enterRule(_localctx, 40, RULE_whileStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			match(While);
			setState(217);
			match(T__1);
			setState(218);
			exp(0);
			setState(219);
			match(T__2);
			setState(220);
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
		enterRule(_localctx, 42, RULE_forStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			match(For);
			setState(223);
			forControl();
			setState(224);
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
		enterRule(_localctx, 44, RULE_forControl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			match(T__1);
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Bool) | (1L << Int) | (1L << String) | (1L << Void) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(227);
				forInit();
				}
			}

			setState(230);
			match(T__0);
			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(231);
				exp(0);
				}
			}

			setState(234);
			match(T__0);
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(235);
				forUpdate();
				}
			}

			setState(238);
			match(T__2);
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
		enterRule(_localctx, 46, RULE_forInit);
		try {
			setState(242);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(240);
				varDec();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(241);
				expList();
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
		enterRule(_localctx, 48, RULE_forUpdate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
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
		public TerminalNode Break() { return getToken(MgParser.Break, 0); }
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
		enterRule(_localctx, 50, RULE_breakStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(Break);
			setState(247);
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
		public TerminalNode Continue() { return getToken(MgParser.Continue, 0); }
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
		enterRule(_localctx, 52, RULE_continueStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(Continue);
			setState(250);
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
		public TerminalNode Return() { return getToken(MgParser.Return, 0); }
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
		enterRule(_localctx, 54, RULE_returnStm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(Return);
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(253);
				exp(0);
				}
			}

			setState(256);
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
		enterRule(_localctx, 56, RULE_emptyStm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
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
		int _startState = 58;
		enterRecursionRule(_localctx, 58, RULE_exp, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
			case T__8:
				{
				_localctx = new PrefixExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(261);
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
				setState(262);
				exp(13);
				}
				break;
			case T__9:
			case T__10:
				{
				_localctx = new PrefixExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(263);
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
				setState(264);
				exp(12);
				}
				break;
			case T__11:
			case T__12:
				{
				_localctx = new PrefixExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(265);
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
				setState(266);
				exp(11);
				}
				break;
			case Null:
				{
				_localctx = new NullExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(267);
				match(Null);
				}
				break;
			case T__1:
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
				setState(268);
				primaryExp(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(299);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(297);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(271);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(272);
						((ArithBinaryExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__13) | (1L << T__14) | (1L << T__15))) != 0)) ) {
							((ArithBinaryExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(273);
						((ArithBinaryExpContext)_localctx).rhs = exp(11);
						}
						break;
					case 2:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(274);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(275);
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
						setState(276);
						((ArithBinaryExpContext)_localctx).rhs = exp(10);
						}
						break;
					case 3:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(277);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(278);
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
						setState(279);
						((ArithBinaryExpContext)_localctx).rhs = exp(9);
						}
						break;
					case 4:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(280);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(281);
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
						setState(282);
						((ArithBinaryExpContext)_localctx).rhs = exp(8);
						}
						break;
					case 5:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(283);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(284);
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
						setState(285);
						((ArithBinaryExpContext)_localctx).rhs = exp(7);
						}
						break;
					case 6:
						{
						_localctx = new ArithBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((ArithBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(286);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(287);
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
						setState(288);
						((ArithBinaryExpContext)_localctx).rhs = exp(6);
						}
						break;
					case 7:
						{
						_localctx = new LogicBinaryExpContext(new ExpContext(_parentctx, _parentState));
						((LogicBinaryExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(289);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(290);
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
						setState(291);
						((LogicBinaryExpContext)_localctx).rhs = exp(5);
						}
						break;
					case 8:
						{
						_localctx = new AssignExpContext(new ExpContext(_parentctx, _parentState));
						((AssignExpContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(292);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(293);
						match(T__4);
						setState(294);
						((AssignExpContext)_localctx).rhs = exp(4);
						}
						break;
					case 9:
						{
						_localctx = new SuffixExpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(295);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(296);
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
				setState(301);
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
		int _startState = 60;
		enterRecursionRule(_localctx, 60, RULE_primaryExp, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(314);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				_localctx = new ParenPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(303);
				match(T__1);
				setState(304);
				exp(0);
				setState(305);
				match(T__2);
				}
				break;
			case 2:
				{
				_localctx = new SelfPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(307);
				match(This);
				}
				break;
			case 3:
				{
				_localctx = new LiteralPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(308);
				literal();
				}
				break;
			case 4:
				{
				_localctx = new CreatePrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(309);
				match(New);
				setState(310);
				creator();
				}
				break;
			case 5:
				{
				_localctx = new VarPrimaryExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(311);
				match(Identifier);
				}
				break;
			case 6:
				{
				_localctx = new FunctCallExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(312);
				match(Identifier);
				setState(313);
				arguments();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(335);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(333);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new MemberSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(316);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(317);
						match(T__29);
						setState(318);
						((MemberSelContext)_localctx).memberName = match(This);
						}
						break;
					case 2:
						{
						_localctx = new MemberSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(319);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(320);
						match(T__29);
						setState(321);
						((MemberSelContext)_localctx).memberName = match(Identifier);
						}
						break;
					case 3:
						{
						_localctx = new MemberSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(322);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(323);
						match(T__29);
						setState(324);
						match(T__1);
						setState(325);
						((MemberSelContext)_localctx).memberName = match(Identifier);
						setState(326);
						match(T__2);
						}
						break;
					case 4:
						{
						_localctx = new MethodSelContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(327);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(328);
						match(T__29);
						setState(329);
						match(Identifier);
						setState(330);
						arguments();
						}
						break;
					case 5:
						{
						_localctx = new ArrayAcsExpContext(new PrimaryExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_primaryExp);
						setState(331);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(332);
						arrayAccessor();
						}
						break;
					}
					} 
				}
				setState(337);
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
		enterRule(_localctx, 62, RULE_literal);
		try {
			setState(341);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PosIntegerConstant:
				enterOuterAlt(_localctx, 1);
				{
				setState(338);
				integerLiteral();
				}
				break;
			case StringConstant:
				enterOuterAlt(_localctx, 2);
				{
				setState(339);
				stringLiteral();
				}
				break;
			case LogicConstant:
				enterOuterAlt(_localctx, 3);
				{
				setState(340);
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
		enterRule(_localctx, 64, RULE_arrayAccessor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(343);
			match(T__30);
			setState(344);
			exp(0);
			setState(345);
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
		enterRule(_localctx, 66, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			match(T__1);
			setState(349);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(348);
				expList();
				}
			}

			setState(351);
			match(T__2);
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
		enterRule(_localctx, 68, RULE_expList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			exp(0);
			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(354);
				match(T__3);
				setState(355);
				exp(0);
				}
				}
				setState(360);
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
		enterRule(_localctx, 70, RULE_creator);
		try {
			int _alt;
			setState(372);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(361);
				simpleType();
				setState(365);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(362);
						arrayCreatorDim();
						}
						} 
					}
					setState(367);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(368);
				simpleType();
				setState(369);
				match(T__1);
				setState(370);
				match(T__2);
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
		enterRule(_localctx, 72, RULE_arrayCreatorDim);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			match(T__30);
			setState(376);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << LogicConstant) | (1L << PosIntegerConstant) | (1L << StringConstant) | (1L << Null) | (1L << New) | (1L << This) | (1L << Identifier))) != 0)) {
				{
				setState(375);
				exp(0);
				}
			}

			setState(378);
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
		enterRule(_localctx, 74, RULE_integerLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
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
		enterRule(_localctx, 76, RULE_stringLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
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
		enterRule(_localctx, 78, RULE_logicLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
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
		enterRule(_localctx, 80, RULE_type);
		try {
			setState(388);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(386);
				simpleType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(387);
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
		enterRule(_localctx, 82, RULE_simpleType);
		try {
			setState(392);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
			case Void:
				enterOuterAlt(_localctx, 1);
				{
				setState(390);
				primitiveType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(391);
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
		enterRule(_localctx, 84, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(394);
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
		enterRule(_localctx, 86, RULE_userType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
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
		enterRule(_localctx, 88, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			simpleType();
			setState(399);
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
		enterRule(_localctx, 90, RULE_arrayDimDecList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(401);
				arrayDimDec();
				}
				}
				setState(404); 
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
		enterRule(_localctx, 92, RULE_arrayDimDec);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(406);
			match(T__30);
			setState(407);
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
		case 29:
			return exp_sempred((ExpContext)_localctx, predIndex);
		case 30:
			return primaryExp_sempred((PrimaryExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 7);
		case 4:
			return precpred(_ctx, 6);
		case 5:
			return precpred(_ctx, 5);
		case 6:
			return precpred(_ctx, 4);
		case 7:
			return precpred(_ctx, 3);
		case 8:
			return precpred(_ctx, 14);
		}
		return true;
	}
	private boolean primaryExp_sempred(PrimaryExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 6);
		case 10:
			return precpred(_ctx, 5);
		case 11:
			return precpred(_ctx, 4);
		case 12:
			return precpred(_ctx, 3);
		case 13:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3;\u019c\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\3\2\7\2b\n\2\f\2\16\2e\13\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\5\3n\n\3\3\4\3\4\3\5\3\5\3\5\3\5\5\5v\n\5\3\5\3\5\3"+
		"\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\7\b\u0085\n\b\f\b\16\b\u0088"+
		"\13\b\3\t\3\t\3\t\3\t\5\t\u008e\n\t\3\n\3\n\3\n\7\n\u0093\n\n\f\n\16\n"+
		"\u0096\13\n\3\13\3\13\3\13\3\f\3\f\7\f\u009d\n\f\f\f\16\f\u00a0\13\f\3"+
		"\f\3\f\3\r\3\r\3\r\3\r\3\r\5\r\u00a9\n\r\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\20\5\20\u00b2\n\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\5\21\u00c1\n\21\3\22\3\22\3\22\3\23\3\23\7\23\u00c8\n"+
		"\23\f\23\16\23\u00cb\13\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\5\25\u00d9\n\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\5\30\u00e7\n\30\3\30\3\30\5\30\u00eb\n\30\3\30\3"+
		"\30\5\30\u00ef\n\30\3\30\3\30\3\31\3\31\5\31\u00f5\n\31\3\32\3\32\3\33"+
		"\3\33\3\33\3\34\3\34\3\34\3\35\3\35\5\35\u0101\n\35\3\35\3\35\3\36\3\36"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u0110\n\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\7\37\u012c\n\37\f\37"+
		"\16\37\u012f\13\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \5 \u013d\n \3 "+
		"\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \7 \u0150\n \f \16 \u0153"+
		"\13 \3!\3!\3!\5!\u0158\n!\3\"\3\"\3\"\3\"\3#\3#\5#\u0160\n#\3#\3#\3$\3"+
		"$\3$\7$\u0167\n$\f$\16$\u016a\13$\3%\3%\7%\u016e\n%\f%\16%\u0171\13%\3"+
		"%\3%\3%\3%\5%\u0177\n%\3&\3&\5&\u017b\n&\3&\3&\3\'\3\'\3(\3(\3)\3)\3*"+
		"\3*\5*\u0187\n*\3+\3+\5+\u018b\n+\3,\3,\3-\3-\3.\3.\3.\3/\6/\u0195\n/"+
		"\r/\16/\u0196\3\60\3\60\3\60\3\60\2\4<>\61\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^\2\f\3\2\n\13\3"+
		"\2\f\r\3\2\16\17\3\2\20\22\3\2\23\24\3\2\25\30\3\2\31\32\3\2\33\35\3\2"+
		"\36\37\3\2*-\2\u01a8\2c\3\2\2\2\4m\3\2\2\2\6o\3\2\2\2\bq\3\2\2\2\nz\3"+
		"\2\2\2\f~\3\2\2\2\16\u0081\3\2\2\2\20\u008d\3\2\2\2\22\u008f\3\2\2\2\24"+
		"\u0097\3\2\2\2\26\u009a\3\2\2\2\30\u00a8\3\2\2\2\32\u00aa\3\2\2\2\34\u00ac"+
		"\3\2\2\2\36\u00ae\3\2\2\2 \u00c0\3\2\2\2\"\u00c2\3\2\2\2$\u00c5\3\2\2"+
		"\2&\u00ce\3\2\2\2(\u00d1\3\2\2\2*\u00da\3\2\2\2,\u00e0\3\2\2\2.\u00e4"+
		"\3\2\2\2\60\u00f4\3\2\2\2\62\u00f6\3\2\2\2\64\u00f8\3\2\2\2\66\u00fb\3"+
		"\2\2\28\u00fe\3\2\2\2:\u0104\3\2\2\2<\u010f\3\2\2\2>\u013c\3\2\2\2@\u0157"+
		"\3\2\2\2B\u0159\3\2\2\2D\u015d\3\2\2\2F\u0163\3\2\2\2H\u0176\3\2\2\2J"+
		"\u0178\3\2\2\2L\u017e\3\2\2\2N\u0180\3\2\2\2P\u0182\3\2\2\2R\u0186\3\2"+
		"\2\2T\u018a\3\2\2\2V\u018c\3\2\2\2X\u018e\3\2\2\2Z\u0190\3\2\2\2\\\u0194"+
		"\3\2\2\2^\u0198\3\2\2\2`b\5\4\3\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2"+
		"\2\2df\3\2\2\2ec\3\2\2\2fg\7\2\2\3g\3\3\2\2\2hi\5\6\4\2ij\7\3\2\2jn\3"+
		"\2\2\2kn\5\b\5\2ln\5\n\6\2mh\3\2\2\2mk\3\2\2\2ml\3\2\2\2n\5\3\2\2\2op"+
		"\5\f\7\2p\7\3\2\2\2qr\5R*\2rs\7;\2\2su\7\4\2\2tv\5\22\n\2ut\3\2\2\2uv"+
		"\3\2\2\2vw\3\2\2\2wx\7\5\2\2xy\5$\23\2y\t\3\2\2\2z{\79\2\2{|\7;\2\2|}"+
		"\5\26\f\2}\13\3\2\2\2~\177\5R*\2\177\u0080\5\16\b\2\u0080\r\3\2\2\2\u0081"+
		"\u0086\5\20\t\2\u0082\u0083\7\6\2\2\u0083\u0085\5\20\t\2\u0084\u0082\3"+
		"\2\2\2\u0085\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087"+
		"\17\3\2\2\2\u0088\u0086\3\2\2\2\u0089\u008e\7;\2\2\u008a\u008b\7;\2\2"+
		"\u008b\u008c\7\7\2\2\u008c\u008e\5<\37\2\u008d\u0089\3\2\2\2\u008d\u008a"+
		"\3\2\2\2\u008e\21\3\2\2\2\u008f\u0094\5\24\13\2\u0090\u0091\7\6\2\2\u0091"+
		"\u0093\5\24\13\2\u0092\u0090\3\2\2\2\u0093\u0096\3\2\2\2\u0094\u0092\3"+
		"\2\2\2\u0094\u0095\3\2\2\2\u0095\23\3\2\2\2\u0096\u0094\3\2\2\2\u0097"+
		"\u0098\5R*\2\u0098\u0099\7;\2\2\u0099\25\3\2\2\2\u009a\u009e\7\b\2\2\u009b"+
		"\u009d\5\30\r\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3"+
		"\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a1\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1"+
		"\u00a2\7\t\2\2\u00a2\27\3\2\2\2\u00a3\u00a9\5\36\20\2\u00a4\u00a9\5\34"+
		"\17\2\u00a5\u00a6\5\32\16\2\u00a6\u00a7\7\3\2\2\u00a7\u00a9\3\2\2\2\u00a8"+
		"\u00a3\3\2\2\2\u00a8\u00a4\3\2\2\2\u00a8\u00a5\3\2\2\2\u00a9\31\3\2\2"+
		"\2\u00aa\u00ab\5\f\7\2\u00ab\33\3\2\2\2\u00ac\u00ad\5\b\5\2\u00ad\35\3"+
		"\2\2\2\u00ae\u00af\7;\2\2\u00af\u00b1\7\4\2\2\u00b0\u00b2\5\22\n\2\u00b1"+
		"\u00b0\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b4\7\5"+
		"\2\2\u00b4\u00b5\5$\23\2\u00b5\37\3\2\2\2\u00b6\u00c1\5&\24\2\u00b7\u00c1"+
		"\5(\25\2\u00b8\u00c1\5*\26\2\u00b9\u00c1\5,\27\2\u00ba\u00c1\5\64\33\2"+
		"\u00bb\u00c1\5\66\34\2\u00bc\u00c1\58\35\2\u00bd\u00c1\5\"\22\2\u00be"+
		"\u00c1\5$\23\2\u00bf\u00c1\5:\36\2\u00c0\u00b6\3\2\2\2\u00c0\u00b7\3\2"+
		"\2\2\u00c0\u00b8\3\2\2\2\u00c0\u00b9\3\2\2\2\u00c0\u00ba\3\2\2\2\u00c0"+
		"\u00bb\3\2\2\2\u00c0\u00bc\3\2\2\2\u00c0\u00bd\3\2\2\2\u00c0\u00be\3\2"+
		"\2\2\u00c0\u00bf\3\2\2\2\u00c1!\3\2\2\2\u00c2\u00c3\5<\37\2\u00c3\u00c4"+
		"\7\3\2\2\u00c4#\3\2\2\2\u00c5\u00c9\7\b\2\2\u00c6\u00c8\5 \21\2\u00c7"+
		"\u00c6\3\2\2\2\u00c8\u00cb\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00ca\3\2"+
		"\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cc\u00cd\7\t\2\2\u00cd"+
		"%\3\2\2\2\u00ce\u00cf\5\f\7\2\u00cf\u00d0\7\3\2\2\u00d0\'\3\2\2\2\u00d1"+
		"\u00d2\7\61\2\2\u00d2\u00d3\7\4\2\2\u00d3\u00d4\5<\37\2\u00d4\u00d5\7"+
		"\5\2\2\u00d5\u00d8\5 \21\2\u00d6\u00d7\7\62\2\2\u00d7\u00d9\5 \21\2\u00d8"+
		"\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9)\3\2\2\2\u00da\u00db\7\64\2\2"+
		"\u00db\u00dc\7\4\2\2\u00dc\u00dd\5<\37\2\u00dd\u00de\7\5\2\2\u00de\u00df"+
		"\5 \21\2\u00df+\3\2\2\2\u00e0\u00e1\7\63\2\2\u00e1\u00e2\5.\30\2\u00e2"+
		"\u00e3\5 \21\2\u00e3-\3\2\2\2\u00e4\u00e6\7\4\2\2\u00e5\u00e7\5\60\31"+
		"\2\u00e6\u00e5\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00ea"+
		"\7\3\2\2\u00e9\u00eb\5<\37\2\u00ea\u00e9\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb"+
		"\u00ec\3\2\2\2\u00ec\u00ee\7\3\2\2\u00ed\u00ef\5\62\32\2\u00ee\u00ed\3"+
		"\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f1\7\5\2\2\u00f1"+
		"/\3\2\2\2\u00f2\u00f5\5\f\7\2\u00f3\u00f5\5F$\2\u00f4\u00f2\3\2\2\2\u00f4"+
		"\u00f3\3\2\2\2\u00f5\61\3\2\2\2\u00f6\u00f7\5F$\2\u00f7\63\3\2\2\2\u00f8"+
		"\u00f9\7\65\2\2\u00f9\u00fa\7\3\2\2\u00fa\65\3\2\2\2\u00fb\u00fc\7\66"+
		"\2\2\u00fc\u00fd\7\3\2\2\u00fd\67\3\2\2\2\u00fe\u0100\7\67\2\2\u00ff\u0101"+
		"\5<\37\2\u0100\u00ff\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u0102\3\2\2\2\u0102"+
		"\u0103\7\3\2\2\u01039\3\2\2\2\u0104\u0105\7\3\2\2\u0105;\3\2\2\2\u0106"+
		"\u0107\b\37\1\2\u0107\u0108\t\2\2\2\u0108\u0110\5<\37\17\u0109\u010a\t"+
		"\3\2\2\u010a\u0110\5<\37\16\u010b\u010c\t\4\2\2\u010c\u0110\5<\37\r\u010d"+
		"\u0110\7.\2\2\u010e\u0110\5> \2\u010f\u0106\3\2\2\2\u010f\u0109\3\2\2"+
		"\2\u010f\u010b\3\2\2\2\u010f\u010d\3\2\2\2\u010f\u010e\3\2\2\2\u0110\u012d"+
		"\3\2\2\2\u0111\u0112\f\f\2\2\u0112\u0113\t\5\2\2\u0113\u012c\5<\37\r\u0114"+
		"\u0115\f\13\2\2\u0115\u0116\t\3\2\2\u0116\u012c\5<\37\f\u0117\u0118\f"+
		"\n\2\2\u0118\u0119\t\6\2\2\u0119\u012c\5<\37\13\u011a\u011b\f\t\2\2\u011b"+
		"\u011c\t\7\2\2\u011c\u012c\5<\37\n\u011d\u011e\f\b\2\2\u011e\u011f\t\b"+
		"\2\2\u011f\u012c\5<\37\t\u0120\u0121\f\7\2\2\u0121\u0122\t\t\2\2\u0122"+
		"\u012c\5<\37\b\u0123\u0124\f\6\2\2\u0124\u0125\t\n\2\2\u0125\u012c\5<"+
		"\37\7\u0126\u0127\f\5\2\2\u0127\u0128\7\7\2\2\u0128\u012c\5<\37\6\u0129"+
		"\u012a\f\20\2\2\u012a\u012c\t\2\2\2\u012b\u0111\3\2\2\2\u012b\u0114\3"+
		"\2\2\2\u012b\u0117\3\2\2\2\u012b\u011a\3\2\2\2\u012b\u011d\3\2\2\2\u012b"+
		"\u0120\3\2\2\2\u012b\u0123\3\2\2\2\u012b\u0126\3\2\2\2\u012b\u0129\3\2"+
		"\2\2\u012c\u012f\3\2\2\2\u012d\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012e"+
		"=\3\2\2\2\u012f\u012d\3\2\2\2\u0130\u0131\b \1\2\u0131\u0132\7\4\2\2\u0132"+
		"\u0133\5<\37\2\u0133\u0134\7\5\2\2\u0134\u013d\3\2\2\2\u0135\u013d\7:"+
		"\2\2\u0136\u013d\5@!\2\u0137\u0138\78\2\2\u0138\u013d\5H%\2\u0139\u013d"+
		"\7;\2\2\u013a\u013b\7;\2\2\u013b\u013d\5D#\2\u013c\u0130\3\2\2\2\u013c"+
		"\u0135\3\2\2\2\u013c\u0136\3\2\2\2\u013c\u0137\3\2\2\2\u013c\u0139\3\2"+
		"\2\2\u013c\u013a\3\2\2\2\u013d\u0151\3\2\2\2\u013e\u013f\f\b\2\2\u013f"+
		"\u0140\7 \2\2\u0140\u0150\7:\2\2\u0141\u0142\f\7\2\2\u0142\u0143\7 \2"+
		"\2\u0143\u0150\7;\2\2\u0144\u0145\f\6\2\2\u0145\u0146\7 \2\2\u0146\u0147"+
		"\7\4\2\2\u0147\u0148\7;\2\2\u0148\u0150\7\5\2\2\u0149\u014a\f\5\2\2\u014a"+
		"\u014b\7 \2\2\u014b\u014c\7;\2\2\u014c\u0150\5D#\2\u014d\u014e\f\4\2\2"+
		"\u014e\u0150\5B\"\2\u014f\u013e\3\2\2\2\u014f\u0141\3\2\2\2\u014f\u0144"+
		"\3\2\2\2\u014f\u0149\3\2\2\2\u014f\u014d\3\2\2\2\u0150\u0153\3\2\2\2\u0151"+
		"\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152?\3\2\2\2\u0153\u0151\3\2\2\2"+
		"\u0154\u0158\5L\'\2\u0155\u0158\5N(\2\u0156\u0158\5P)\2\u0157\u0154\3"+
		"\2\2\2\u0157\u0155\3\2\2\2\u0157\u0156\3\2\2\2\u0158A\3\2\2\2\u0159\u015a"+
		"\7!\2\2\u015a\u015b\5<\37\2\u015b\u015c\7\"\2\2\u015cC\3\2\2\2\u015d\u015f"+
		"\7\4\2\2\u015e\u0160\5F$\2\u015f\u015e\3\2\2\2\u015f\u0160\3\2\2\2\u0160"+
		"\u0161\3\2\2\2\u0161\u0162\7\5\2\2\u0162E\3\2\2\2\u0163\u0168\5<\37\2"+
		"\u0164\u0165\7\6\2\2\u0165\u0167\5<\37\2\u0166\u0164\3\2\2\2\u0167\u016a"+
		"\3\2\2\2\u0168\u0166\3\2\2\2\u0168\u0169\3\2\2\2\u0169G\3\2\2\2\u016a"+
		"\u0168\3\2\2\2\u016b\u016f\5T+\2\u016c\u016e\5J&\2\u016d\u016c\3\2\2\2"+
		"\u016e\u0171\3\2\2\2\u016f\u016d\3\2\2\2\u016f\u0170\3\2\2\2\u0170\u0177"+
		"\3\2\2\2\u0171\u016f\3\2\2\2\u0172\u0173\5T+\2\u0173\u0174\7\4\2\2\u0174"+
		"\u0175\7\5\2\2\u0175\u0177\3\2\2\2\u0176\u016b\3\2\2\2\u0176\u0172\3\2"+
		"\2\2\u0177I\3\2\2\2\u0178\u017a\7!\2\2\u0179\u017b\5<\37\2\u017a\u0179"+
		"\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u017d\7\"\2\2\u017d"+
		"K\3\2\2\2\u017e\u017f\7$\2\2\u017fM\3\2\2\2\u0180\u0181\7%\2\2\u0181O"+
		"\3\2\2\2\u0182\u0183\7#\2\2\u0183Q\3\2\2\2\u0184\u0187\5T+\2\u0185\u0187"+
		"\5Z.\2\u0186\u0184\3\2\2\2\u0186\u0185\3\2\2\2\u0187S\3\2\2\2\u0188\u018b"+
		"\5V,\2\u0189\u018b\5X-\2\u018a\u0188\3\2\2\2\u018a\u0189\3\2\2\2\u018b"+
		"U\3\2\2\2\u018c\u018d\t\13\2\2\u018dW\3\2\2\2\u018e\u018f\7;\2\2\u018f"+
		"Y\3\2\2\2\u0190\u0191\5T+\2\u0191\u0192\5\\/\2\u0192[\3\2\2\2\u0193\u0195"+
		"\5^\60\2\u0194\u0193\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0194\3\2\2\2\u0196"+
		"\u0197\3\2\2\2\u0197]\3\2\2\2\u0198\u0199\7!\2\2\u0199\u019a\7\"\2\2\u019a"+
		"_\3\2\2\2\"cmu\u0086\u008d\u0094\u009e\u00a8\u00b1\u00c0\u00c9\u00d8\u00e6"+
		"\u00ea\u00ee\u00f4\u0100\u010f\u012b\u012d\u013c\u014f\u0151\u0157\u015f"+
		"\u0168\u016f\u0176\u017a\u0186\u018a\u0196";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}