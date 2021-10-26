import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.home.servicegenerator.api.ASTProcessingSchema;

import java.util.function.BiFunction;

public class ArticlesModelProcessingSchema implements ASTProcessingSchema {
    @Override
    public BiFunction<ClassOrInterfaceDeclaration, Object, ClassOrInterfaceDeclaration> postProcessClassOrInterfaceDeclaration() {
        return (ClassOrInterfaceDeclaration n, Object arg) ->
            n.addAnnotation(
                    new NormalAnnotationExpr(
                            new Name("org.springframework.data.mongodb.core.mapping.Document"),
                            NodeList.nodeList(new MemberValuePair("collection", new StringLiteralExpr("articles")))
                    )
            );
    }
}
