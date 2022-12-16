package org.openapitools.codegen.languages;

import static org.openapitools.codegen.utils.StringUtils.camelize;

import java.io.File;
import org.openapitools.codegen.*;
import org.openapitools.codegen.utils.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.media.Schema;

public class PhpLaravelLgServerCodegen
        extends AbstractPhpCodegen {

    public static final String PROJECT_NAME = "PhpLaravelLiongate";

    static final Logger LOGGER = LoggerFactory.getLogger(
            PhpLaravelLgServerCodegen.class);

    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    public String getName() {
        return "php-laravel-lg";
    }

    public String getHelp() {
        return "Generates models, routes and controllers for use in a php laravel project.";
    }

    public PhpLaravelLgServerCodegen() {
        super();

        variableNamingConvention = "camelCase";

        outputFolder = "";
        srcBasePath = "";
        modelTemplateFiles.put("model.mustache", ".php");
        apiTemplateFiles.put("api.mustache", ".php");
        embeddedTemplateDir = templateDir = "php-laravel-lg";
        apiPackage = "App.Http.Controllers.Generated";
        modelPackage = "App.Http.Models.Generated";

        apiTestTemplateFiles.clear();
        apiDocTemplateFiles.clear();
        modelDocTemplateFiles.clear();

        supportingFiles.add(new SupportingFile("routes.mustache", "routes/Generated", "apigen.php"));

        additionalProperties.put("apiPackageNamespace", apiPackage.replace(".", "\\"));
        additionalProperties.put("modelPackageNamespace", modelPackage.replace(".", "\\"));
    }

    @Override
    public String toApiName(String name) {
        if (name.isEmpty()) {
            name = "Default";
        }

        String implClassname = camelize(name) + "Api";
        additionalProperties.put("implClassname", implClassname);

        return "Abstract" + implClassname;
    }

    @Override
    public String apiFileFolder() {
        return outputFolder +
                File.separator +
                toSrcPath(adjustPackagePath(apiPackage), srcBasePath);
    }

    @Override
    public String modelFileFolder() {
        return outputFolder +
                File.separator +
                toSrcPath(adjustPackagePath(modelPackage), srcBasePath);
    }

    /*
     * The directory containing the 'App' namespace in laravel
     * is called 'app'. This method takes care of that translation.
     */
    private String adjustPackagePath(String packagePath) {
        if (packagePath.startsWith("App")) {
            char[] pathChars = packagePath.toCharArray();
            pathChars[0] = 'a';
            return String.valueOf(pathChars);
        }
        return packagePath;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public String toDefaultValue(Schema p) {
        if(p.getDefault() != null) {
            if(ModelUtils.isStringSchema(p)) {
                return "'" + p.getDefault() + "'";
            }

            return p.getDefault().toString();
        }

        if(ModelUtils.isStringSchema(p)) {
            return "''";
        }
        if(ModelUtils.isArraySchema(p)) {
            return "[]";
        }

        return "null";
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        property.dataType = property.dataType.replace(".", "\\");
    }
}
