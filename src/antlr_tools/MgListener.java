package antlr_tools;// Generated from Mg.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MgParser}.
 */
public interface MgListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MgParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(MgParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(MgParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterDec(MgParser.DecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitDec(MgParser.DecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#varDec}.
	 * @param ctx the parse tree
	 */
	void enterVarDec(MgParser.VarDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#varDec}.
	 * @param ctx the parse tree
	 */
	void exitVarDec(MgParser.VarDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#varDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclaratorList(MgParser.VarDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#varDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclaratorList(MgParser.VarDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#varDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVarDeclarator(MgParser.VarDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#varDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVarDeclarator(MgParser.VarDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#functDec}.
	 * @param ctx the parse tree
	 */
	void enterFunctDec(MgParser.FunctDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#functDec}.
	 * @param ctx the parse tree
	 */
	void exitFunctDec(MgParser.FunctDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#functDecParaList}.
	 * @param ctx the parse tree
	 */
	void enterFunctDecParaList(MgParser.FunctDecParaListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#functDecParaList}.
	 * @param ctx the parse tree
	 */
	void exitFunctDecParaList(MgParser.FunctDecParaListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#functDecPara}.
	 * @param ctx the parse tree
	 */
	void enterFunctDecPara(MgParser.FunctDecParaContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#functDecPara}.
	 * @param ctx the parse tree
	 */
	void exitFunctDecPara(MgParser.FunctDecParaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#classDec}.
	 * @param ctx the parse tree
	 */
	void enterClassDec(MgParser.ClassDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#classDec}.
	 * @param ctx the parse tree
	 */
	void exitClassDec(MgParser.ClassDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(MgParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(MgParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#classBodyDec}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDec(MgParser.ClassBodyDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#classBodyDec}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDec(MgParser.ClassBodyDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#constructorDec}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDec(MgParser.ConstructorDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#constructorDec}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDec(MgParser.ConstructorDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#stm}.
	 * @param ctx the parse tree
	 */
	void enterStm(MgParser.StmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#stm}.
	 * @param ctx the parse tree
	 */
	void exitStm(MgParser.StmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#expStm}.
	 * @param ctx the parse tree
	 */
	void enterExpStm(MgParser.ExpStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#expStm}.
	 * @param ctx the parse tree
	 */
	void exitExpStm(MgParser.ExpStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#blockStm}.
	 * @param ctx the parse tree
	 */
	void enterBlockStm(MgParser.BlockStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#blockStm}.
	 * @param ctx the parse tree
	 */
	void exitBlockStm(MgParser.BlockStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#varDecStm}.
	 * @param ctx the parse tree
	 */
	void enterVarDecStm(MgParser.VarDecStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#varDecStm}.
	 * @param ctx the parse tree
	 */
	void exitVarDecStm(MgParser.VarDecStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#ifStm}.
	 * @param ctx the parse tree
	 */
	void enterIfStm(MgParser.IfStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#ifStm}.
	 * @param ctx the parse tree
	 */
	void exitIfStm(MgParser.IfStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#whileStm}.
	 * @param ctx the parse tree
	 */
	void enterWhileStm(MgParser.WhileStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#whileStm}.
	 * @param ctx the parse tree
	 */
	void exitWhileStm(MgParser.WhileStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#forStm}.
	 * @param ctx the parse tree
	 */
	void enterForStm(MgParser.ForStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#forStm}.
	 * @param ctx the parse tree
	 */
	void exitForStm(MgParser.ForStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(MgParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(MgParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(MgParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(MgParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(MgParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(MgParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#breakStm}.
	 * @param ctx the parse tree
	 */
	void enterBreakStm(MgParser.BreakStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#breakStm}.
	 * @param ctx the parse tree
	 */
	void exitBreakStm(MgParser.BreakStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#continueStm}.
	 * @param ctx the parse tree
	 */
	void enterContinueStm(MgParser.ContinueStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#continueStm}.
	 * @param ctx the parse tree
	 */
	void exitContinueStm(MgParser.ContinueStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#returnStm}.
	 * @param ctx the parse tree
	 */
	void enterReturnStm(MgParser.ReturnStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#returnStm}.
	 * @param ctx the parse tree
	 */
	void exitReturnStm(MgParser.ReturnStmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#emptyStm}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStm(MgParser.EmptyStmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#emptyStm}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStm(MgParser.EmptyStmContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterPrefixExp(MgParser.PrefixExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitPrefixExp(MgParser.PrefixExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterAssignExp(MgParser.AssignExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitAssignExp(MgParser.AssignExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arithBinaryExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterArithBinaryExp(MgParser.ArithBinaryExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arithBinaryExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitArithBinaryExp(MgParser.ArithBinaryExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code suffixExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterSuffixExp(MgParser.SuffixExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code suffixExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitSuffixExp(MgParser.SuffixExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primitiveExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveExp(MgParser.PrimitiveExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primitiveExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveExp(MgParser.PrimitiveExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNullExp(MgParser.NullExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNullExp(MgParser.NullExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logicBinaryExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterLogicBinaryExp(MgParser.LogicBinaryExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logicBinaryExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitLogicBinaryExp(MgParser.LogicBinaryExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberSel}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterMemberSel(MgParser.MemberSelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberSel}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitMemberSel(MgParser.MemberSelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterVarPrimaryExp(MgParser.VarPrimaryExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitVarPrimaryExp(MgParser.VarPrimaryExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code createPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterCreatePrimaryExp(MgParser.CreatePrimaryExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code createPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitCreatePrimaryExp(MgParser.CreatePrimaryExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodSel}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterMethodSel(MgParser.MethodSelContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodSel}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitMethodSel(MgParser.MethodSelContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterLiteralPrimaryExp(MgParser.LiteralPrimaryExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitLiteralPrimaryExp(MgParser.LiteralPrimaryExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functCallExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterFunctCallExp(MgParser.FunctCallExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functCallExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitFunctCallExp(MgParser.FunctCallExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayAcsExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterArrayAcsExp(MgParser.ArrayAcsExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayAcsExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitArrayAcsExp(MgParser.ArrayAcsExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterParenPrimaryExp(MgParser.ParenPrimaryExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitParenPrimaryExp(MgParser.ParenPrimaryExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selfPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterSelfPrimaryExp(MgParser.SelfPrimaryExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selfPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitSelfPrimaryExp(MgParser.SelfPrimaryExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(MgParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(MgParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#arrayAccessor}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccessor(MgParser.ArrayAccessorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#arrayAccessor}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccessor(MgParser.ArrayAccessorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(MgParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(MgParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#expList}.
	 * @param ctx the parse tree
	 */
	void enterExpList(MgParser.ExpListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#expList}.
	 * @param ctx the parse tree
	 */
	void exitExpList(MgParser.ExpListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(MgParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(MgParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#arrayCreatorDim}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorDim(MgParser.ArrayCreatorDimContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#arrayCreatorDim}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorDim(MgParser.ArrayCreatorDimContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(MgParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(MgParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(MgParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(MgParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#logicLiteral}.
	 * @param ctx the parse tree
	 */
	void enterLogicLiteral(MgParser.LogicLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#logicLiteral}.
	 * @param ctx the parse tree
	 */
	void exitLogicLiteral(MgParser.LogicLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MgParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MgParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#simpleType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleType(MgParser.SimpleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#simpleType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleType(MgParser.SimpleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(MgParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(MgParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#userType}.
	 * @param ctx the parse tree
	 */
	void enterUserType(MgParser.UserTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#userType}.
	 * @param ctx the parse tree
	 */
	void exitUserType(MgParser.UserTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void enterArrayType(MgParser.ArrayTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#arrayType}.
	 * @param ctx the parse tree
	 */
	void exitArrayType(MgParser.ArrayTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#arrayDimDecList}.
	 * @param ctx the parse tree
	 */
	void enterArrayDimDecList(MgParser.ArrayDimDecListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#arrayDimDecList}.
	 * @param ctx the parse tree
	 */
	void exitArrayDimDecList(MgParser.ArrayDimDecListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MgParser#arrayDimDec}.
	 * @param ctx the parse tree
	 */
	void enterArrayDimDec(MgParser.ArrayDimDecContext ctx);
	/**
	 * Exit a parse tree produced by {@link MgParser#arrayDimDec}.
	 * @param ctx the parse tree
	 */
	void exitArrayDimDec(MgParser.ArrayDimDecContext ctx);
}