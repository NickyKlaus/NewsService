import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.home.servicegenerator.api.ASTProcessingSchema;

import java.util.function.BiFunction;

public class ConfigurationProcessingSchema implements ASTProcessingSchema {
    @Override
    public BiFunction<ClassOrInterfaceDeclaration, Object, ClassOrInterfaceDeclaration> postProcessClassOrInterfaceDeclaration() {
        return (ClassOrInterfaceDeclaration n, Object arg) -> {
            //Register repository into Spring application class
            if (n.getAnnotations().contains(new MarkerAnnotationExpr("SpringBootApplication"))) {
                n.addAnnotation(
                        new NormalAnnotationExpr(
                                new Name("org.springframework.data.mongodb.repository.config.EnableMongoRepositories"),
                                NodeList.nodeList(
                                        new MemberValuePair("basePackageClasses", new ClassExpr().setType("com.home.newsservice.NewsRepository"))
                                )));
                return n;
            }
            return n;
        };
    }
}
