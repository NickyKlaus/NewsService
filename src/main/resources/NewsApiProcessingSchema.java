import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.home.servicegenerator.api.ASTProcessingSchema;

import java.util.function.BiFunction;

public class NewsApiProcessingSchema implements ASTProcessingSchema {

    @Override
    public BiFunction<ClassOrInterfaceDeclaration, Object, ClassOrInterfaceDeclaration> postProcessClassOrInterfaceDeclaration() {
        return (ClassOrInterfaceDeclaration n, Object arg) -> {
            var classContainsNewsGetMethod = n.getMethods().stream()
                    .anyMatch(
                            method -> method.isAnnotationPresent("org.springframework.web.bind.annotation.RequestMapping") &&
                                    method.getNameAsString().equals("newsGet")
                    );

            if (!classContainsNewsGetMethod) {
                //Add repository field
                n.addField(
                        new ClassOrInterfaceType().setName("com.home.newsservice.NewsRepository"),
                        "newsRepository",
                        Modifier.Keyword.PRIVATE,
                        Modifier.Keyword.FINAL
                );

                //Inject repository to constructor
                n.getConstructorByParameterTypes("ObjectMapper", "HttpServletRequest")
                        .ifPresent(
                                constructorDeclaration -> {
                                    var constructorBody = constructorDeclaration
                                            .getBody()
                                            .addStatement("this.newsRepository = newsRepository;");
                                    constructorDeclaration
                                            .addParameter("com.home.newsservice.NewsRepository", "newsRepository")
                                            .setBody(constructorBody);
                                }
                        );

                //Override default method 'newsGet' returning response with all articles from news repository
                var methodBody = new BlockStmt()
                        .setStatements(NodeList.nodeList(
                                new ReturnStmt()
                                        .setExpression(
                                                new ObjectCreationExpr()
                                                        .setType("org.springframework.http.ResponseEntity")
                                                        .setDiamondOperator()
                                                        .setArguments(
                                                                NodeList.nodeList(
                                                                        new MethodCallExpr()
                                                                                .setName("newsRepository.findAll"),
                                                                        new TypeExpr(new ClassOrInterfaceType().setName("org.springframework.http.HttpStatus.OK"))
                                                                )

                                                        )
                                        )
                        ));
                n.addMethod("newsGet", Modifier.Keyword.PUBLIC)
                        .setAnnotations(NodeList.nodeList(new MarkerAnnotationExpr("java.lang.Override")))
                        .setPublic(true)
                        .setType("org.springframework.http.ResponseEntity")
                        .setBody(methodBody);
            }
            return n;
        };
    }
}
