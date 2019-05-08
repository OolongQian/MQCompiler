// this update is based on the observation that the outer most scope of any program is declaration
grammar Mg;

// ---- Program ----

prog
    :   dec* EOF
    ;

dec :   globalDec ';'
    |   functDec
    |   classDec
    ;

globalDec
    :   varDec
    ;

functDec
    :   type functName=Identifier '(' functDecParaList? ')' blockStm
    ;

classDec
    :   'class' className=Identifier classBody
    ;

varDec
    :   type varDeclaratorList
    ;

varDeclaratorList
    :   varDeclarator (',' varDeclarator)*
    ;

varDeclarator
    :   Identifier
    |   Identifier '=' exp
    ;

functDecParaList
    :   functDecPara (',' functDecPara)*
    ;

functDecPara
    :   type Identifier
    ;

classBody
    : '{' classBodyDec* '}'
    ;

classBodyDec
    :   constructorDec
    |   methodDec
    |   fieldDec ';'
    ;

fieldDec
    :   varDec
    ;

methodDec
    :   functDec
    ;

constructorDec
    :   className=Identifier '(' functDecParaList? ')' blockStm
    ;

stm
    :   varDecStm
    |   ifStm
    |   whileStm
    |   forStm
    |   breakStm
    |   continueStm
    |   returnStm
    |   expStm
    |   blockStm
    |   emptyStm
    ;

expStm
    :   exp ';'
    ;

blockStm
    :   '{' stm* '}'
    ;

/*** check ***/
varDecStm
    :   varDec ';'
    ;

/*** check ***/
ifStm
    :   If '(' exp ')' then_=stm (Else else_=stm)?
    ;

/*** check ***/
whileStm
    :   While '(' exp ')' stm
    ;

/*** check ***/
forStm
    :   For forControl stm
    ;

/*** check ***/
forControl
    :   '(' forInit? ';' exp? ';' forUpdate? ')'
    ;

forInit
    :   varDec
    |   expList
    ;

forUpdate
    :   expList
    ;

// '(' inital=stm? ';' check=exp? ';' update=stm? ')'

breakStm
    :   'break' ';'
    ;

continueStm
    :   'continue' ';'
    ;

returnStm
    :   'return' exp? ';'
    ;

emptyStm
    :   ';'
    ;

exp
    :   exp op=('++' | '--') # suffixExp // lValExp=exp /*** check ***/
    |   <assoc=right> op=('++'|'--') exp # prefixExp // lValExp=exp /*** check ***/
    |   <assoc=right> op=('+' | '-') exp # prefixExp
    |   <assoc=right> op=('!' | '~') exp # prefixExp
    |   lhs=exp op=('*'|'/'|'%') rhs=exp # arithBinaryExp /*** check ***/
    |   lhs=exp op=('+'|'-') rhs=exp # arithBinaryExp
    |   lhs=exp op=('<<'|'>>') rhs=exp # arithBinaryExp
    |   lhs=exp op=('>'|'>='|'<'|'<=') rhs=exp # arithBinaryExp
    |   lhs=exp op=('=='|'!=') rhs=exp # arithBinaryExp
    |   lhs=exp op='&' rhs=exp # arithBinaryExp
    |   lhs=exp op='^' rhs=exp # arithBinaryExp
    |   lhs=exp op='|' rhs=exp # arithBinaryExp
    |   lhs=exp op=('&&'|'||') rhs=exp # logicBinaryExp /*** check ***/
    |   lhs=exp '=' rhs=exp # assignExp /*** check ***/
    |   Null # nullExp /*** check ***/
    |   primaryExp # primitiveExp /*** check ***/
    ;

primaryExp
    :   '(' exp ')' # parenPrimaryExp
    |   This # selfPrimaryExp
    |   literal # literalPrimaryExp
    |   New creator # createPrimaryExp
    |   Identifier # varPrimaryExp
    |   primaryExp '.' memberName=This # memberSel
    |   primaryExp '.' memberName=Identifier # memberSel
    |   primaryExp '.' '(' memberName=Identifier ')' # memberSel
    |   primaryExp '.' Identifier arguments # methodSel
    |   primaryExp arrayAccessor # arrayAcsExp
    |   Identifier arguments # functCallExp
    ;

literal
    :   integerLiteral
    |   stringLiteral
    |   logicLiteral
    ;


arrayAccessor
    :   '[' exp ']'
    ;

arguments
    :   '(' expList? ')'
    ;

expList
    :   exp (',' exp)*
    ;

creator
    :   simpleType arrayCreatorDim*
    |   simpleType '(' ')'
    ;

arrayCreatorDim
    :   '[' exp? ']'
    ;

integerLiteral
    :   PosIntegerConstant
    ;

stringLiteral
    :   StringConstant
    ;

logicLiteral
    :   LogicConstant
    ;

type
    :   simpleType
    |   arrayType
    ;

simpleType
    :   primitiveType
    |   userType
    ;

primitiveType
    :   Bool
    |   Int
    |   String
    |   Void
    ;

userType
    :   Identifier
    ;

arrayType
    :   simpleType arrayDimDecList
    ;

arrayDimDecList
    :   arrayDimDec+
    ;

arrayDimDec
    :   '[' ']'
    ;

//// ---- Lexer Tokens ----
///**
//    The order of lexer rules represents priority
//    */
// ---- Constant ----
LogicConstant
    :   True
    |   False
    ;

PosIntegerConstant
    :   Digit
    |   DigitNoZero Digit*
    ;

fragment Letter
    :   [a-zA-Z]
    ;

fragment Underscore
    :   '_'
    ;

fragment DigitNoZero
    :   [1-9]
    ;

fragment Digit
    :   [0-9]
    ;

//StringConstant
//    :   '"' StringChar* '"'
//    ;
//
// I don't know what happens
//fragment StringChar
//    :   ~[ \n\r\t]
//    |   ' '
//    |   '\n'
//    |   '\\'
//    ;

StringConstant
	:	'"' StringCharacters? '"'
	;

fragment
StringCharacters
	:	StringCharacter+
	;

fragment
StringCharacter
	:	~["\\]
	|	EscapeSequence
	;

// §3.10.6 Escape Sequences for Character and String Literals

fragment
EscapeSequence
	:	'\\' [btnfr"'\\]
	;

// ---- Special ----
// **** 1. /**/ is undefined, regarded as feature.
// Mode isn't compatible with parser...
/**
    Modes allow you to group lexical rules by context.
    The lexer can only return tokens matched by entering a rule in the current mode.
    Lexers start out in the so-called default mode.
    All rules are considered to be within the default mode unless you specify a mode command. */
LineComment
    :   '//' ~[\r\n]* -> skip
    ;

// This can tolerate example: /* /* */ as a comment.
BlockComment
    :   '/*' (BlockComment|.)*? '*/' -> skip
    ;

// ---- White space ----
// cannot be put in advance because of priority
WhiteSpace
    :   [ \t]+ -> skip
    ;

NewLine
    :   '\r'? '\n' -> skip
    ;

// ---- Reserved Keywords ----
Bool                : 'bool' ; // for reg types
Int                 : 'int' ;
String              : 'string' ;
Void                : 'void' ;
Null                : 'null' ;
True                : 'true' ; // for boolean
False               : 'false' ;
If                  : 'if' ; // for keywords
Else                : 'else';
For                 : 'for' ;
While               : 'while' ;
Break               : 'break' ;
Continue            : 'continue' ;
Return              : 'return' ;
New                 : 'new' ;
Class               : 'class' ;
This                : 'this' ;

// ---- Identifier ----
Identifier
    :   (Letter) (Letter|Underscore|Digit)*
    ;
