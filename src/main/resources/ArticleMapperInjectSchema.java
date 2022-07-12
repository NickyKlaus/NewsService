import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.home.origami.api.ASTProcessingSchema;
import com.home.origami.api.context.Context;

import java.util.function.BiFunction;

import static java.lang.String.format;

public class ArticleMapperInjectSchema implements ASTProcessingSchema {
    @Override
    public BiFunction<CompilationUnit, Context, CompilationUnit> preProcessCompilationUnit() {
        return (CompilationUnit n, Context context) -> {
            var mapperTypeName = context
                    .getPropertyByName("mapperTypeName")
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    format(CONTEXT_PREFERENCE_IS_NOT_SET_ERROR_MESSAGE, "mapperTypeName")))
                    .toString();
            n.addImport(mapperTypeName);
            return n;
        };
    }

    @Override
    public BiFunction<ClassOrInterfaceDeclaration, Context, ClassOrInterfaceDeclaration> postProcessClassOrInterfaceDeclaration() {
        return (ClassOrInterfaceDeclaration n, Context context) -> {
            var mapperTypeName = context
                    .getPropertyByName("mapperTypeName")
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    format(CONTEXT_PREFERENCE_IS_NOT_SET_ERROR_MESSAGE, "mapperTypeName")))
                    .toString();
            return n;
        };
    }

    private static final String CONTEXT_PREFERENCE_IS_NOT_SET_ERROR_MESSAGE = "%s is not set";
}
