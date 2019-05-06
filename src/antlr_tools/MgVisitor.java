package antlr_tools;// Generated from Mg.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MgParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MgVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MgParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(MgParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDec(MgParser.DecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#globalDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalDec(MgParser.GlobalDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#functDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctDec(MgParser.FunctDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#classDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDec(MgParser.ClassDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#varDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDec(MgParser.VarDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#varDeclaratorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclaratorList(MgParser.VarDeclaratorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#varDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDeclarator(MgParser.VarDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#functDecParaList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctDecParaList(MgParser.FunctDecParaListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#functDecPara}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctDecPara(MgParser.FunctDecParaContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(MgParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#classBodyDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBodyDec(MgParser.ClassBodyDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#fieldDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDec(MgParser.FieldDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#methodDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDec(MgParser.MethodDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#constructorDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDec(MgParser.ConstructorDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#stm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStm(MgParser.StmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#expStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpStm(MgParser.ExpStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#blockStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStm(MgParser.BlockStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#varDecStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecStm(MgParser.VarDecStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#ifStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStm(MgParser.IfStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#whileStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStm(MgParser.WhileStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#forStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStm(MgParser.ForStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(MgParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(MgParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(MgParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#breakStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStm(MgParser.BreakStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#continueStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStm(MgParser.ContinueStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#returnStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStm(MgParser.ReturnStmContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#emptyStm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStm(MgParser.EmptyStmContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixExp(MgParser.PrefixExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignExp(MgParser.AssignExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arithBinaryExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithBinaryExp(MgParser.ArithBinaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code suffixExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixExp(MgParser.SuffixExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primitiveExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveExp(MgParser.PrimitiveExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullExp(MgParser.NullExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicBinaryExp}
	 * labeled alternative in {@link MgParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicBinaryExp(MgParser.LogicBinaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberSel}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberSel(MgParser.MemberSelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarPrimaryExp(MgParser.VarPrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code createPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatePrimaryExp(MgParser.CreatePrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodSel}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodSel(MgParser.MethodSelContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literalPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralPrimaryExp(MgParser.LiteralPrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functCallExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctCallExp(MgParser.FunctCallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayAcsExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAcsExp(MgParser.ArrayAcsExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenPrimaryExp(MgParser.ParenPrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selfPrimaryExp}
	 * labeled alternative in {@link MgParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfPrimaryExp(MgParser.SelfPrimaryExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(MgParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#arrayAccessor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAccessor(MgParser.ArrayAccessorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(MgParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#expList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpList(MgParser.ExpListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(MgParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#arrayCreatorDim}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreatorDim(MgParser.ArrayCreatorDimContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#integerLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(MgParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(MgParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#logicLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicLiteral(MgParser.LogicLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(MgParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#simpleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleType(MgParser.SimpleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(MgParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#userType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserType(MgParser.UserTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#arrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(MgParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#arrayDimDecList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDimDecList(MgParser.ArrayDimDecListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MgParser#arrayDimDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDimDec(MgParser.ArrayDimDecContext ctx);
}