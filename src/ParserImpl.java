import java.util.*;

@SuppressWarnings("unchecked")
public class ParserImpl
{
    public static Boolean _debug = true;
    void Debug(String message)
    {
        if(_debug)
            System.out.println(message);
    }

    // This is for chained symbol table.
    // This includes the global scope only at this moment.
    Env env = new Env(null);
    // this stores the root of parse tree, which will be used to print parse tree and run the parse tree
    ParseTree.Program parsetree_program = null;

    Object program____decllist(Object s1) throws Exception
    {
        //TODO
        // 1. check if decllist has main function having no parameters and returns int type
        // 2. assign the root, whose type is ParseTree.Program, to parsetree_program
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        parsetree_program = new ParseTree.Program(decllist);
        return parsetree_program;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object decllist____decllist_decl(Object s1, Object s2) throws Exception
    {
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        ParseTree.FuncDecl                decl = (ParseTree.FuncDecl           )s2;
        decllist.add(decl);
        return decllist;
    }
    Object decllist____eps() throws Exception
    {
        return new ArrayList<ParseTree.FuncDecl>();
    }
    Object decl____funcdecl(Object s1) throws Exception
    {
        return s1;
    }
    Object primtype____NUM(Object s1) throws Exception
    {
        ParseTree.TypeSpec typespec = new ParseTree.TypeSpec(Type.NUM);
        return typespec;
    }
    Object typespec____primtype(Object s1)
    {
        ParseTree.TypeSpec primtype = (ParseTree.TypeSpec)s1;
        return primtype;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    Object fundecl____FUNC_IDENT_TYPEOF_typespec_LPAREN_params_RPAREN_BEGIN_localdecls_stmtlist_END (Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7, Object s8, Object s9, Object s10, Object s11) throws Exception
//    {//
//        //
//        // 1. add function_type_info object (name, return type, params) into the global scope of env
//        // 2. create a new symbol table on top of env
//        // 3. add parameters into top-local scope of env
//        // 4. etc.
//        Token func = (Token) s1;
//        Token ident = (Token) s2;
//        Token typeOf = (Token) s3;
//        ParseTree.TypeSpec typeSpec = (ParseTree.TypeSpec) s4;
//        env.Put(ident.lexeme, Type.FUNC);
//        Token lParen = (Token) s5;
//        env = new Env(env);
//        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s6;
//        for(ParseTree.Param param: params){
//            // probably uses ParamInfo?
//        }
//        Token rParen = (Token) s7;
//        Token begin = (Token) s8;
//        ArrayList<ParseTree.LocalDecl>   localdecls = (ArrayList<ParseTree.LocalDecl>  )s9;
//        ArrayList<ParseTree.Stmt>        stmtlist   = (ArrayList<ParseTree.Stmt>       )s10;
//        Token                            end        = (Token                           )s11;
//
////        return new ParseTree.FuncDecl(ident.lexeme, typeSpec, );
////        throw new Exception();//
////        return null;
//
//        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(ident.lexeme, typeSpec, params, localdecls, stmtlist);
//        return funcdecl;
//    }

    Object fundecl____FUNC_IDENT_TYPEOF_typespec_LPAREN_params_RPAREN_BEGIN_localdecls_10X_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7, Object s8, Object s9) throws IdentifierDefinedException {
        Token ident = (Token) s2;
        ParseTree.TypeSpec typeSpec = (ParseTree.TypeSpec) s4;
        if(env.m.containsKey(ident.lexeme))
            throw new IdentifierDefinedException(ident);
        env.Put(ident.lexeme, funcType(typeSpec.typename));
        ////////////////////////////////////////////////
        env = new Env(env);
        env.funcId = ident.lexeme;
        env.hasRet = false;
        //////////////////////////////////////////////////
        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s6;
        for(ParseTree.Param param:params){
            if(env.m.containsKey(param.info.id))
                throw new IdentifierDefinedException(param.info);
            env.Put(param.info.id, param.info.type);
        }
        ArrayList<ParseTree.LocalDecl> localDecls = (ArrayList<ParseTree.LocalDecl>) s9;
        for(ParseTree.LocalDecl localDecl:localDecls){
            if(env.m.containsKey(localDecl.info.id))
                throw new IdentifierDefinedException(localDecl.info);
            env.Put(localDecl.info.id, localDecl.info.type);
        }

        return null;
    }

    Object fundecl____FUNC_IDENT_TYPEOF_typespec_LPAREN_params_RPAREN_BEGIN_localdecls_X10_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7, Object s8, Object s9, Object s10, Object s11, Object s12) throws SemanticException {
        Token ident = (Token) s2;
        ParseTree.TypeSpec rettype = (ParseTree.TypeSpec) s4;
        ArrayList<ParseTree.Param> params = (ArrayList<ParseTree.Param>) s6;
        ArrayList<ParseTree.LocalDecl> localDecls = (ArrayList<ParseTree.LocalDecl>) s9;
        ArrayList<ParseTree.Stmt> stmts = (ArrayList<ParseTree.Stmt>) s11;
        if(!env.hasRet)
            throw new SemanticException(ident, String.format("Function %s() should return at least one value.", env.funcId));
        env = env.prev;
        return new ParseTree.FuncDecl(ident.lexeme, rettype, params, localDecls, stmts);
    }

    Object params____eps() throws Exception 
    {
        return new ArrayList<ParseTree.Param>();
    }

    Object stmtlist____stmtlist_stmt(Object s1, Object s2) throws Exception
    {
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>)s1;
        ParseTree.Stmt            stmt     = (ParseTree.Stmt           )s2;
        stmtlist.add(stmt);
        return stmtlist;
    }
    Object stmtlist____eps() throws Exception
    {
        return new ArrayList<ParseTree.Stmt>();
    }

    Object stmt____assignstmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.AssignStmt);
        return s1;
    }
    Object stmt____returnstmt  (Object s1) throws Exception
    {
        assert(s1 instanceof ParseTree.ReturnStmt);
        return s1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object assignstmt____IDENT_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3, Object s4) throws Exception
    {
        //TODO
        // 1. check if ident.value_type matches with expr.value_type
        // 2. etc.
        // e. create and return node
        Token          id     = (Token         )s1;
        Token          assign = (Token         )s2;
        ParseTree.Expr expr   = (ParseTree.Expr)s3;
        String type = (String) env.Get(id.lexeme);
        if(type==null){
            throw new VariableNotDefinedException(id);
        } else if (!type.equals(expr.info.type)) {
            throw new VariableTypeException(id, type, expr.info.type);
        }


//        Object id_type = env.Get(id.lexeme);
//        {
//            // check if expr.type matches with id_type
//            if(id_type.equals("num")
//                && (expr instanceof ParseTree.ExprNumLit)
//                )
//                {} // ok
//            else if(id_type.equals("num")
//                && (expr instanceof ParseTree.ExprFuncCall)
//                && (env.Get(((ParseTree.ExprFuncCall)expr).ident).equals("num()"))
//                )
//            {} // ok
//            else
//            {
//                throw new Exception("semantic error");
//            }
//        }
        ParseTree.AssignStmt stmt = new ParseTree.AssignStmt(id.lexeme, expr);
        stmt.ident_reladdr = 1;
        return stmt;
    }
    Object returnstmt____RETURN_expr_SEMI(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr.value_type matches with the current function return type
        // 2. etc.
        // 3. create and return node
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        if(!expr.info.type.equals(unFuncType((String)env.Get(env.funcId))))
            throw new SemanticException(expr.info.lineno, expr.info.column, String.format("Function %s() should return %s value, instead of %s value.", env.funcId, unFuncType((String)env.Get(env.funcId)), expr.info.type)) ;
        env.hasRet=true;
        return new ParseTree.ReturnStmt(expr);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object localdecls____localdecls_localdecl(Object s1, Object s2)
    {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>)s1;
        ParseTree.LocalDecl            localdecl  = (ParseTree.LocalDecl           )s2;
        localdecls.add(localdecl);
        return localdecls;
    }
    Object localdecls____eps() throws Exception
    {
        return new ArrayList<ParseTree.LocalDecl>();
    }
    Object localdecl____VAR_IDENT_TYPEOF_typespec_SEMI(Object s1, Object s2, Object s3, Object s4, Object s5) throws IdentifierDefinedException {
        Token var = (Token) s1;
        Token id = (Token) s2;
        Token typeOf = (Token) s3;
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec)s4;
        Token semi = (Token)s5;
        ParseTree.LocalDecl localdecl = new ParseTree.LocalDecl(id.lexeme, typespec);
        localdecl.info.id = id.lexeme;
        localdecl.info.type = typespec.typename;
        localdecl.info.lineno = id.lineno;
        localdecl.info.column = id.column;
//        if(env.m.containsKey(id.lexeme)){
//            throw new IdentifierDefinedException(localdecl.info);
//        }
        localdecl.reladdr = 1;
//        env.Put(id.lexeme, typespec.typename);
        return localdecl;
    }
    Object args____eps() throws Exception
    {
        return new ArrayList<ParseTree.Expr>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object expr____expr_ADD_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        if(!(expr1.info.type.equals(Type.NUM) && expr2.info.type.equals(Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprAdd expr3 = new ParseTree.ExprAdd(expr1,expr2);
        expr3.info.type = Type.NUM;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }
    Object expr____expr_EQ_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals(expr2.info.type)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprEq expr3 = new ParseTree.ExprEq(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }
    Object expr____LPAREN_expr_RPAREN(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. create and return node whose value_type is the same to the expr.value_type
        Token          lparen = (Token         )s1;
        ParseTree.Expr expr1   = (ParseTree.Expr)s2;
        Token          rparen = (Token         )s3;
        ParseTree.ExprParen expr2 = new ParseTree.ExprParen(expr1);
        expr2.info.type = expr1.info.type;
        expr2.info.lineno = lparen.lineno;
        expr2.info.column = lparen.column;
        return expr2;
    }
    Object expr____IDENT(Object s1) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is variable type
        // 3. etc.
        // 4. create and return node that has the value_type of the id.lexeme
        Token id = (Token)s1;
        ParseTree.ExprIdent expr = new ParseTree.ExprIdent(id.lexeme);
        expr.info.lineno = id.lineno;
        expr.info.column = id.column;
        expr.info.id = id.lexeme;
        String type = (String) env.Get(id.lexeme);
        if(type==null){
            throw new VariableNotDefinedException(id);
        } else if (type.equals(Type.FUNC)) {
            throw new SemanticException(id, String.format("Identifier %s should be non-function type.",id.lexeme));
        }
        expr.info.type = type;
        expr.reladdr = 1;
        return expr;
    }
    Object expr____IDENT_LPAREN_args_RPAREN(Object s1, Object s2, Object s3, Object s4) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is function type
        // 3. check if the number and types of env(id.lexeme).params match with those of args
        // 4. etc.
        // 5. create and return node that has the value_type of env(id.lexeme).return_type
        Token                    id   = (Token                   )s1;
        ArrayList<ParseTree.Arg> args = (ArrayList<ParseTree.Arg>)s3;
//        Object func_attr = env.Get(id.lexeme);
//        {
//            // check if argument types match with function param types
//            if(env.Get(id.lexeme).equals("num()")
//                && (args.size() == 0)
//                )
//            {} // ok
//            else
//            {
//                throw new Exception("semantic error");
//            }
//        }
        ParseTree.ExprFuncCall expr = new ParseTree.ExprFuncCall(id.lexeme, args);
        String funcType = (String) env.Get(id.lexeme);
        expr.info.id = id.lexeme;
        if(funcType==null){
            throw new FunctionNotDefinedException(id);
        }
        expr.info.type=unFuncType(funcType);
        return expr;
    }
    Object expr____NUMLIT(Object s1) throws Exception
    {
        // 1. create and return node that has int type
        Token token = (Token)s1;
        ParseTree.ExprNumLit expr1 = new ParseTree.ExprNumLit(Double.parseDouble(token.lexeme));
        expr1.info.type = Type.NUM;
        expr1.info.lineno = token.lineno;
        expr1.info.column = token.column;
        return expr1;
    }
    /////////////////////////////////////////////////////////////////////////////
    Object params____paramlist(Object s1) throws Exception
    {
        List<ParseTree.Param> paramList = (List<ParseTree.Param>) s1;
        return paramList;
    }

    Object paramlist____paramlist_COMMA_param(Object s1, Object s2, Object s3) throws Exception
    {
        List<ParseTree.Param> paramList = (List<ParseTree.Param>) s1;
        Token token = (Token) s2;
        ParseTree.Param param = (ParseTree.Param) s3;
        paramList.add(param);
        return paramList;
    }

    Object paramlist____param(Object s1) throws Exception
    {
        ParseTree.Param param = (ParseTree.Param) s1;
        List<ParseTree.Param> paramList =  new ArrayList<>();
        paramList.add(param);
        return paramList;
    }

    Object param____IDENT_TYPEOF_typespec(Object s1, Object s2, Object s3) throws Exception
    {
        Token ident = (Token) s1;
        Token token = (Token) s2;
        ParseTree.TypeSpec typeSpec = (ParseTree.TypeSpec) s3;
        ParseTree.Param param = new ParseTree.Param(ident.lexeme, typeSpec);
        param.info.id = ident.lexeme;
        param.info.type = typeSpec.typename;
        return param;
    }

    Object typespec____primtype_LBRACKET_RBRACKET(Object s1, Object s2, Object s3) throws Exception
    {
        ParseTree.TypeSpec type = (ParseTree.TypeSpec) s1;
        Token lBracket = (Token) s2;
        Token rBracket = (Token) s3;
        return new ParseTree.TypeSpec(type.typename+"[]");
    }

    Object primtype____BOOL(Object s1) throws Exception
    {
        return new ParseTree.TypeSpec(Type.BOOL);
    }

    Object stmt____printstmt(Object s1) throws Exception
    {
        ParseTree.Stmt stmt = (ParseTree.PrintStmt) s1;
        return stmt;
    }

    Object stmt____ifstmt(Object s1) throws Exception
    {
        ParseTree.Stmt stmt = (ParseTree.IfStmt) s1;
        return stmt;
    }

    Object stmt____whilestmt(Object s1) throws Exception
    {
        ParseTree.Stmt stmt = (ParseTree.WhileStmt) s1;
        return stmt;
    }

    Object stmt____compoundstmt(Object s1) throws Exception
    {
        ParseTree.Stmt stmt = (ParseTree.CompoundStmt) s1;
        return stmt;
    }

    Object assignstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) throws Exception
    {
        Token ident = (Token) s1;
        String type = (String) env.Get(ident.lexeme);
        if(type==null)
            throw new ArrayNotDefinedException(ident);
        if(!type.contains("[]"))
            throw new IndexNonarrayException(ident);
        Token lBracket = (Token) s2;
        ParseTree.Expr expr1 = (ParseTree.Expr) s3;
        if(!expr1.info.type.equals(Type.NUM)){
            throw new ArrayIndexTypeException(expr1);
        }
        Token rBracket = (Token) s4;
        Token assign = (Token) s5;
        ParseTree.Expr expr2 = (ParseTree.Expr) s6;
        String arrType = unArrType((String)env.Get(ident.lexeme));
        if(!expr2.info.type.equals(arrType))
            throw new ArrayElementTypeException(ident, arrType, expr2.info.type);
        Token semi = (Token) s7;
        return new ParseTree.AssignStmtForArray(ident.lexeme, expr1, expr2);
    }

    Object printstmt____PRINT_expr_SEMI(Object s1, Object s2, Object s3) throws Exception
    {
        Token print = (Token) s1;
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        Token semi = (Token) s3;
        return new ParseTree.PrintStmt(expr);
    }

    Object ifstmt____IF_expr_THEN_stmtlist_ELSE_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) throws Exception
    {
        Token if_ = (Token) s1;
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        if(!expr.info.type.equals(Type.BOOL)){
            throw new SemanticException(expr, "Condition of if or while statement should be bool value.");
        }
        Token then_ = (Token) s3;
        ArrayList<ParseTree.Stmt> stmtList1 = (ArrayList<ParseTree.Stmt>) s4;
        Token else_ = (Token) s5;
        ArrayList<ParseTree.Stmt> stmtList2 = (ArrayList<ParseTree.Stmt>) s6;
        Token end_ = (Token) s7;
        return new ParseTree.IfStmt(expr, stmtList1, stmtList2);
    }

    Object whilestmt____WHILE_expr_BEGIN_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception
    {
        Token while_ = (Token) s1;
        ParseTree.Expr expr = (ParseTree.Expr) s2;
        Token begin_ = (Token) s3;
        ArrayList<ParseTree.Stmt> stmtList = (ArrayList<ParseTree.Stmt>) s4;
        Token end_ = (Token) s5;
        return new ParseTree.WhileStmt(expr, stmtList);
    }

//    Object compoundstmt____BEGIN_localdecls_stmtlist_END(Object s1, Object s2, Object s3, Object s4) throws Exception
//    {
//        Token begin = (Token) s1;
//        env = new Env(env);
//        ArrayList<ParseTree.LocalDecl> localDecls = (ArrayList<ParseTree.LocalDecl>) s2;
//        ArrayList<ParseTree.Stmt> stmtList = (ArrayList<ParseTree.Stmt>) s3;
//        Token end = (Token) s4;
//        env = env.prev;
//        return new ParseTree.CompoundStmt(localDecls, stmtList);
//    }

    Object compoundstmt____BEGIN_localdecls_10X_stmtlist_END(Object s1, Object s2) throws IdentifierDefinedException {
        env = new Env(env);
        env.funcId = env.prev.funcId;
        ArrayList<ParseTree.LocalDecl> localDecls = (ArrayList<ParseTree.LocalDecl>) s2;
        for(ParseTree.LocalDecl localDecl:localDecls){
            if(env.m.containsKey(localDecl.info.id))
                throw new IdentifierDefinedException(localDecl.info);
            env.Put(localDecl.info.id, localDecl.info.type);
        }

        return null;
    }

    Object compoundstmt____BEGIN_localdecls_X10_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5){
        ArrayList<ParseTree.LocalDecl> localDecls = (ArrayList<ParseTree.LocalDecl>) s2;
        ArrayList<ParseTree.Stmt> stmts = (ArrayList<ParseTree.Stmt>) s4;
        env = env.prev;

        return new ParseTree.CompoundStmt(localDecls, stmts);
    }


    Object args____arglist(Object s1) throws Exception
    {
        List<ParseTree.Arg> argList = (List<ParseTree.Arg>) s1;
        return argList;
    }

    Object arglist____arglist_COMMA_expr(Object s1, Object s2, Object s3) throws Exception
    {
        List<ParseTree.Arg> argList = (List<ParseTree.Arg>) s1;
        Token comma = (Token) s2;
        ParseTree.Expr expr = (ParseTree.Expr) s3;
        argList.add(new ParseTree.Arg(expr));
        return argList;
    }

    Object arglist____expr(Object s1) throws Exception
    {
        ParseTree.Expr expr = (ParseTree.Expr) s1;
        List<ParseTree.Arg> argList = new ArrayList<>();
        argList.add(new ParseTree.Arg(expr));
        return argList;
    }

    Object expr____expr_SUB_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        if(!(expr1.info.type.equals(Type.NUM) && expr2.info.type.equals(Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprSub expr3 = new ParseTree.ExprSub(expr1,expr2);
        expr3.info.type = Type.NUM;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_MUL_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        if(!(expr1.info.type.equals(Type.NUM) && expr2.info.type.equals(Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprMul expr3 = new ParseTree.ExprMul(expr1,expr2);
        expr3.info.type = Type.NUM;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_DIV_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        if(!(expr1.info.type.equals(Type.NUM) && expr2.info.type.equals(Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprDiv expr3 = new ParseTree.ExprDiv(expr1,expr2);
        expr3.info.type = Type.NUM;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_MOD_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        if(!(expr1.info.type.equals(Type.NUM) && expr2.info.type.equals(Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprMod expr3 = new ParseTree.ExprMod(expr1,expr2);
        expr3.info.type = Type.NUM;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_NE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals(expr2.info.type)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprNe expr3 = new ParseTree.ExprNe(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_LE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals( Type.NUM) && expr2.info.type.equals( Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprLe expr3 = new ParseTree.ExprLe(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_LT_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals( Type.NUM) && expr2.info.type.equals( Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprLt expr3 = new ParseTree.ExprLt(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_GE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals( Type.NUM) && expr2.info.type.equals( Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprGe expr3 = new ParseTree.ExprGe(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_GT_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals( Type.NUM) && expr2.info.type.equals( Type.NUM)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprGt expr3 = new ParseTree.ExprGt(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_AND_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals( Type.BOOL )&& expr2.info.type.equals( Type.BOOL)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprAnd expr3 = new ParseTree.ExprAnd(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____expr_OR_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals( Type.BOOL) && expr2.info.type.equals( Type.BOOL)))
            throw new BinaryOperationException(oper, expr1, expr2);
        ParseTree.ExprOr expr3 = new ParseTree.ExprOr(expr1,expr2);
        expr3.info.type = Type.BOOL;
        expr3.info.lineno = expr1.info.lineno;
        expr3.info.column = expr1.info.column;
        return expr3;
    }

    Object expr____NOT_expr(Object s1, Object s2) throws Exception
    {
        Token          oper  = (Token         )s1;
        ParseTree.Expr expr1 = (ParseTree.Expr)s2;
        // check if expr1.type matches with expr2.type
        if(!(expr1.info.type.equals( Type.BOOL)))
            throw new SemanticException(oper, String.format("Unary operation %s cannot be used with %s value.",oper.lexeme, expr1.info.type));
        ParseTree.ExprNot expr2 = new ParseTree.ExprNot(expr1);
        expr2.info.type = Type.BOOL;
        expr2.info.lineno = expr1.info.lineno;
        expr2.info.column = expr1.info.column;
        return expr2;
    }

    Object expr____BOOLLIT(Object s1) throws Exception
    {
        Token b = (Token) s1;
        ParseTree.ExprBoolLit expr1 = new ParseTree.ExprBoolLit(Boolean.parseBoolean(b.lexeme));
        expr1.info.type= Type.BOOL;
        expr1.info.lineno = b.lineno;
        expr1.info.column = b.column;
        return expr1;
    }

    Object expr____NEW_primtype_LBRACKET_expr_RBRACKET(Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception
    {
        Token new_ = (Token) s1;
        ParseTree.TypeSpec type = (ParseTree.TypeSpec) s2;
        Token lBracket = (Token) s3;
        ParseTree.Expr expr = (ParseTree.Expr) s4;
        Token rBracket = (Token) s5;
        ParseTree.ExprNewArray ena = new ParseTree.ExprNewArray(type, expr);
        ena.info.type = type.typename + "[]";
        return ena;
    }

    Object expr____IDENT_LBRACKET_expr_RBRACKET(Object s1, Object s2, Object s3, Object s4) throws Exception
    {
        Token ident = (Token) s1;
        String type = (String) env.Get(ident.lexeme);
        if(type==null)
            throw new ArrayNotDefinedException(ident);
        if(!type.contains("[]"))
            throw new IndexNonarrayException(ident);
        Token lBracket = (Token) s2;
        ParseTree.Expr expr = (ParseTree.Expr) s3;
        if(!expr.info.type.equals(Type.NUM)){
            throw new ArrayIndexTypeException(expr);
        }
//        System.out.println(String.format("%s[%s]", ident.lexeme,expr.info.type));
        Token rBracket = (Token) s4;
        ParseTree.ExprArrayElem eae = new ParseTree.ExprArrayElem(ident.lexeme, expr);
        eae.info.type = unArrType(type);
        return eae;
    }

    Object expr____IDENT_DOT_SIZE(Object s1, Object s2, Object s3) throws Exception
    {
        Token ident = (Token) s1;
        Token dot = (Token) s2;
        Token size = (Token) s3;
        return new ParseTree.ExprArraySize(ident.lexeme);
    }

    public static class SemanticException extends Exception{
        public SemanticException(int lineno, int column, String error){
            super(String.format("[Error at %s:%s] %s",lineno,column,error));
        }
        public SemanticException(Token t, String error){
            this(t.lineno, t.column, error);
        }

        public SemanticException(ParseTree.Expr expr, String error) {
            this(expr.info.lineno, expr.info.column, error);
        }
    }



    private static class IndexNonarrayException extends SemanticException{
        public IndexNonarrayException(Token token){
            super(token.lineno, token.column, String.format("Identifier %s should be array variable.", token.lexeme));
        }
    }
    private static class ArrayElementTypeException extends SemanticException{
        public ArrayElementTypeException(Token token, String type, String wrongtype){
            super(token.lineno, token.column, String.format("Element of array %s should have %s value, instead of %s value.", token.lexeme, type, wrongtype));
        }
    }

    private static class ArrayIndexTypeException extends SemanticException{
        public ArrayIndexTypeException(ParseTree.Expr expr){
            super(expr.info.lineno, expr.info.column, "Array index must be num value.");
        }
    }

    private static class VariableTypeException extends SemanticException{
        public VariableTypeException(Token ident, String type, String wrongtype){
            super(ident.lineno, ident.column, String.format("Variable %s should have %s value, instead of %s value.", ident.lexeme, type, wrongtype ));
        }
    }

    private static class VariableNotDefinedException extends SemanticException{
        public VariableNotDefinedException(Token id) {
            super(id.lineno,id.column,String.format("Variable %s is not defined.",id.lexeme));
        }
    }
    private static class ArrayNotDefinedException extends SemanticException{
        public ArrayNotDefinedException(Token id) {
            super(id.lineno,id.column,String.format("Array %s is not defined.",id.lexeme));
        }
    }

    private static class FunctionNotDefinedException extends SemanticException{

        public FunctionNotDefinedException(Token t) {
            super(t, String.format("Function %s() is not defined.", t.lexeme));
        }
    }

    private static class IdentifierDefinedException extends SemanticException{

        public IdentifierDefinedException(int lineno, int column, String id) {
            super(lineno, column, String.format("Identifier %s is already defined.", id));
        }

        public IdentifierDefinedException(ParseTreeInfo info) {
            this(info.lineno, info.column, info.id);
        }

        public IdentifierDefinedException(Token t) {
            this(t.lineno,t.column,t.lexeme);
        }
    }

    private static class BinaryOperationException extends SemanticException{
        public BinaryOperationException(Token oper, ParseTree.Expr expr1, ParseTree.Expr expr2){
            super(oper.lineno,oper.column,String.format("Binary operation %s cannot be used with %s and %s values.",oper.lexeme, expr1.info.type, expr2.info.type));
        }
    }

    public static class Type {
        public final static String NUM = "num";
        public final static String BOOL = "bool";
        public final static String FUNC = "func";
    }
    private String arrType(String type){
        return type+"[]";
    }
    private String unArrType(String arr){
        return arr.substring(0, arr.lastIndexOf('['));
    }
    private String funcType(String type){
        return type+"()";
    }
    private String unFuncType(String func){
        return func.substring(0, func.lastIndexOf('('));
    }
}
