package ca.uwaterloo.jrefactoring.utility;

import ca.uwaterloo.jrefactoring.action.Action;
import ca.uwaterloo.jrefactoring.action.DefaultAction;
import ca.uwaterloo.jrefactoring.node.RFNodeDifference;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

public class Transformer {

    private static Action action = new DefaultAction();

    public static Action getAction() {
        return action;
    }

    public static void setAction(Action action) {
        Transformer.action = action;
    }

    public static void transform(RFNodeDifference diff) {
        action.execute(diff);
    }

    public static Type typeFromBinding(AST ast, ITypeBinding typeBinding) {
        if( ast == null )
            throw new NullPointerException("ast is null");
        if( typeBinding == null )
            throw new NullPointerException("typeBinding is null");

        if( typeBinding.isPrimitive() ) {
            return ast.newPrimitiveType(
                    PrimitiveType.toCode(typeBinding.getName()));
        }

        if( typeBinding.isCapture() ) {
            ITypeBinding wildCard = typeBinding.getWildcard();
            WildcardType capType = ast.newWildcardType();
            ITypeBinding bound = wildCard.getBound();
            if( bound != null ) {
                capType.setBound(typeFromBinding(ast, bound), wildCard.isUpperbound());
            }
            return capType;
        }

        if( typeBinding.isArray() ) {
            Type elType = typeFromBinding(ast, typeBinding.getElementType());
            return ast.newArrayType(elType, typeBinding.getDimensions());
        }

        if( typeBinding.isParameterizedType() ) {
            ParameterizedType type = ast.newParameterizedType(
                    typeFromBinding(ast, typeBinding.getErasure()));

            @SuppressWarnings("unchecked")
            List<Type> newTypeArgs = type.typeArguments();
            for( ITypeBinding typeArg : typeBinding.getTypeArguments() ) {
                newTypeArgs.add(typeFromBinding(ast, typeArg));
            }

            return type;
        }

        // simple or raw type
        //String qualName = typeBinding.getQualifiedName();
        String name = typeBinding.getName();
        if( "".equals(name) ) {
            throw new IllegalArgumentException("No name for type binding.");
        }
        return ast.newSimpleType(ast.newName(name));
    }
}