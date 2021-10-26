import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.home.servicegenerator.api.ASTProcessingSchema;

import java.util.function.BiFunction;

public class GenerateRepositoryProcessingSchema implements ASTProcessingSchema {
    @Override
    public BiFunction<CompilationUnit, Object, CompilationUnit> preProcessCompilationUnit() {
        return (CompilationUnit n, Object arg) -> {
            n.setPackageDeclaration("com.home.newsservice")
                    .addInterface("NewsRepository", Modifier.Keyword.PUBLIC)
                    .addMarkerAnnotation("org.springframework.stereotype.Repository")
                    .setExtendedTypes(
                            NodeList.nodeList(
                                    new ClassOrInterfaceType()
                                            .setName("org.springframework.data.repository.CrudRepository")
                                            .setTypeArguments(
                                                    NodeList.nodeList(
                                                            new ClassOrInterfaceType().setName("com.home.newsservice.model.Article"),
                                                            new ClassOrInterfaceType().setName("java.lang.Long")
                                                    ))));
            return n;
        };
    }
}
