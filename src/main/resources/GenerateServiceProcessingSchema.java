import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.home.servicegenerator.api.ASTProcessingSchema;

import java.util.function.BiFunction;

public class GenerateServiceProcessingSchema implements ASTProcessingSchema {
    @Override
    public BiFunction<ClassOrInterfaceDeclaration, Object, ClassOrInterfaceDeclaration> postProcessClassOrInterfaceDeclaration() {
        return (ClassOrInterfaceDeclaration n, Object arg) ->
                n.setName(n.getNameAsString() + "Impl")
                    .setInterface(false)
                        .addMarkerAnnotation("org.springframework.stereotype.Service")
                    .setImplementedTypes(
                            NodeList.nodeList(
                                    new ClassOrInterfaceType().setName("com.home.newsservice.NewsService")));
    }
}