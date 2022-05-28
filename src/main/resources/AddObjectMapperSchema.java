import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.home.servicegenerator.api.ASTProcessingSchema;
import com.home.servicegenerator.api.context.Context;

import java.util.function.BiFunction;

import static java.lang.String.format;

public class AddObjectMapperSchema implements ASTProcessingSchema {
    @Override
    public BiFunction<CompilationUnit, Context, CompilationUnit> preProcessCompilationUnit() {
        return (CompilationUnit n, Context context) -> {
            System.out.println("!!!!!!"+n.getPrimaryTypeName());
            return n.addImport("org.springframework.context.annotation.Bean")
                    .addImport("org.springframework.context.annotation.Primary")
                    .addImport("org.springframework.http.converter.json.Jackson2ObjectMapperBuilder")
                    .addImport("com.fasterxml.jackson.databind.SerializationFeature");
        };
    }

    @Override
    public BiFunction<ClassOrInterfaceDeclaration, Context, ClassOrInterfaceDeclaration> preProcessClassOrInterfaceDeclaration() {
        final String SPRING_BOOT_APPLICATION_FULL = "org.springframework.boot.autoconfigure.SpringBootApplication";
        final String SPRING_BOOT_APPLICATION_SHORT = "SpringBootApplication";

        return (ClassOrInterfaceDeclaration n, Context context) -> {
            // Bean method body
            final BlockStmt beanBody = new BlockStmt()
                    .setStatements(NodeList.nodeList(
                            new ReturnStmt()
                                    .setExpression(
                                            new MethodCallExpr()
                                                    .setScope(
                                                            new MethodCallExpr()
                                                                    .setScope(
                                                                            new NameExpr("builder"))
                                                                    .setName("build"))
                                                    .setName("configure")
                                            .setArguments(
                                                    NodeList.nodeList(
                                                            new NameExpr()
                                                                    .setName("SerializationFeature.WRITE_DATES_AS_TIMESTAMPS"),
                                                            new BooleanLiteralExpr(false)
                                                    )
                                            )
                                    )));

            //Register ObjectMapper bean into Spring application class
            if (n.getAnnotationByName(SPRING_BOOT_APPLICATION_FULL).isPresent() ||
                    n.getAnnotationByName(SPRING_BOOT_APPLICATION_SHORT).isPresent()) {
                n.addMethod("objectMapper", Modifier.Keyword.PUBLIC)
                        .addMarkerAnnotation("Bean")
                        .addMarkerAnnotation("Primary")
                        .setBody(beanBody);
            }
            return n;
        };
    }
}