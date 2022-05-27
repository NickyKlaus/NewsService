import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.home.servicegenerator.api.ASTProcessingSchema;
import com.home.servicegenerator.api.context.Context;

import java.util.function.BiFunction;

import static java.lang.String.format;

public class NewsMapperSchema implements ASTProcessingSchema {
    @Override
    public BiFunction<CompilationUnit, Context, CompilationUnit> preProcessCompilationUnit() {
        return (CompilationUnit n, Context context) -> {
            var newsMapperPackage = context.get("mapperPackage", String.class);
            var newsMapperClass = context.get("mapperClassName", String.class);

            ClassOrInterfaceDeclaration mapper =
                    new ClassOrInterfaceDeclaration()
                            .setInterface(true)
                            .setName(newsMapperClass)
                            .setModifier(Modifier.Keyword.PUBLIC, true);

            n.setPackageDeclaration(newsMapperPackage)
                    .addImport("org.mapstruct.Mapper")
                    .addImport("com.home.newsservice.model.Article")
                    .addType(mapper);

            return n;
        };
    }

    @Override
    public BiFunction<ClassOrInterfaceDeclaration, Context, ClassOrInterfaceDeclaration> preProcessClassOrInterfaceDeclaration() {
        return (ClassOrInterfaceDeclaration n, Context context) -> {
            n.addAnnotation(
                    new NormalAnnotationExpr().setName("Mapper"));
            var method =
                    n.addMethod("to")
                            .setType("Article")
                            .setParameters(NodeList.nodeList(new Parameter().setName("article").setType("Article")))
                            .setBody(null);
            return n;
        };
    }
}
