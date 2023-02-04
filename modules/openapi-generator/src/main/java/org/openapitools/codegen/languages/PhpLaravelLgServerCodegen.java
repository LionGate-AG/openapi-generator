package org.openapitools.codegen.languages;

import static org.openapitools.codegen.utils.StringUtils.camelize;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.openapitools.codegen.*;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.utils.ModelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.media.Schema;

public class PhpLaravelLgServerCodegen
        extends AbstractPhpCodegen {

    public static final String PROJECT_NAME = "PhpLaravelLiongate";

    static final Logger LOGGER = LoggerFactory.getLogger(
            PhpLaravelLgServerCodegen.class);

    private Map<String, String> modelFilenames = new HashMap<String, String>();

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
        modelPackage = "App.Http.Resources.Generated";

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
    public String toModelFilename(String name) {
        if(modelFilenames.containsKey(name)) {
            return camelize(modelFilenames.get(name));
        }

        return camelize(name);
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
        if(property.isArray) {
            property.items.dataType = property.items.dataType.replace(".", "\\");
        }
    }

    @Override
    public void postProcessParameter(CodegenParameter parameter) {
        parameter.dataType = parameter.dataType.replace(".", "\\");
    }

    @Override
    public ModelsMap postProcessModels(ModelsMap objs) {
        objs = super.postProcessModels(objs);

        ModelMap models = objs.getModels().get(0);
        CodegenModel model = models.getModel();

        modelFilenames.put(model.getName(), model.getClassFilename());

        if(model.getInterfaces() != null && model.getInterfaces().contains("DbModel")) {
            model.vendorExtensions.put("x-isResource", Boolean.TRUE);
            model.setClassname(model.getClassname()+"Resource");
            modelFilenames.put(model.getName(), model.getClassFilename()+"Resource");
        } else {
            model.vendorExtensions.put("x-isResource", Boolean.FALSE);
        }
        if(model.getInterfaces() != null && model.getInterfaces().contains("PageableList")) {
            model.vendorExtensions.put("x-isCollection", Boolean.TRUE);
            model.setClassname(getCollectionClassName(model));
            modelFilenames.put(model.getName(), getCollectionClassName(model));
        } else {
            model.vendorExtensions.put("x-isCollection", Boolean.FALSE);
        }

        for(CodegenProperty var : model.getVars()) {
            var.nameInSnakeCase = var.nameInSnakeCase.toLowerCase(Locale.ROOT);
            if(var.getFormat() != null && var.getFormat().equals("url")){
                var.vendorExtensions.put("x-isUrl", Boolean.TRUE);
            } else {
                var.vendorExtensions.put("x-isUrl", Boolean.FALSE);
            }
        }

        return objs;
    }

    private String getCollectionClassName(CodegenModel model) {
        for(CodegenProperty var : model.getVars()) {
            if(var.isArray) {
                return var.items.baseType + "Collection";
            }
        }

        return model.getClassname() + "Collection";
    }
}
