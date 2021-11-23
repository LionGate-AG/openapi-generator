/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.languages;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.CodegenType;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenSecurity;
import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.meta.GeneratorMetadata;
import org.openapitools.codegen.meta.Stability;
import org.openapitools.codegen.meta.features.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static org.openapitools.codegen.utils.StringUtils.camelize;

public class PhpLavaLumenServerCodegen extends AbstractPhpCodegen {
    private final Logger LOGGER = LoggerFactory.getLogger(PhpLavaLumenServerCodegen.class);

    public static final String USER_CLASSNAME_KEY = "userClassname";
    public static final String PSR7_IMPLEMENTATION = "psr7Implementation";

    protected String groupId = "org.openapitools";
    protected String artifactId = "openapi-server";
    protected String authDirName = "Auth";
    protected String authPackage = "";
    protected String psr7Implementation = "slim-psr7";
    protected String interfacesDirName = "Interfaces";
    protected String interfacesPackage = "";

    public PhpLavaLumenServerCodegen() {
        super();

        // PDS skeleton recommends tests folder
        // https://github.com/php-pds/skeleton
        this.testBasePath = "tests";

        modifyFeatureSet(features -> features
                .includeDocumentationFeatures(DocumentationFeature.Readme)
                .wireFormatFeatures(EnumSet.of(WireFormatFeature.JSON, WireFormatFeature.XML))
                .securityFeatures(EnumSet.noneOf(SecurityFeature.class))
                .excludeGlobalFeatures(
                        GlobalFeature.XMLStructureDefinitions,
                        GlobalFeature.Callbacks,
                        GlobalFeature.LinkObjects,
                        GlobalFeature.ParameterStyling
                )
                .excludeSchemaSupportFeatures(
                        SchemaSupportFeature.Polymorphism
                )
                .includeClientModificationFeatures(ClientModificationFeature.MockServer)
        );

        generatorMetadata = GeneratorMetadata.newBuilder(generatorMetadata)
                .stability(Stability.STABLE)
                .build();

        // clear import mapping (from default generator) as slim does not use it
        // at the moment
        importMapping.clear();

        /*
         * Api Package.  Optional, if needed, this can be used in templates
         */
        apiPackage = "Http.Controllers";

        /*
         * Model Package.  Optional, if needed, this can be used in templates
         */
        modelPackage = "Http.Models";

        invokerPackage = "";

        srcBasePath = "";

        /*
         * customPathPrefix.  Optional, if needed, this is used routes templates
         */
        additionalProperties.put("customPathPrefix", "api");

        variableNamingConvention = "camelCase";
        artifactVersion = "1.0.0";
        setInvokerPackage(invokerPackage);
        
        authPackage = invokerPackage + "\\" + authDirName;
        interfacesPackage = invokerPackage + "\\" + interfacesDirName;
        outputFolder = "generated-code" + File.separator + "Lava";
        apiTemplateFiles.put("api.mustache", ".php");

        modelTestTemplateFiles.put("model_test.mustache", ".php");
        // no doc files
        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();

        embeddedTemplateDir = templateDir = "php-lava-lumen";

        additionalProperties.put(CodegenConstants.GROUP_ID, groupId);
        additionalProperties.put(CodegenConstants.ARTIFACT_ID, artifactId);

        // override cliOptions from AbstractPhpCodegen
        updateOption(AbstractPhpCodegen.VARIABLE_NAMING_CONVENTION, "camelCase");

        // Slim 4 can use any PSR-7 implementation
        // https://www.slimframework.com/docs/v4/concepts/value-objects.html
        CliOption psr7Option = new CliOption(PSR7_IMPLEMENTATION,
                "Slim 4 provides its own PSR-7 implementation so that it works out of the box. However, you are free to replace Slim’s default PSR-7 objects with a third-party implementation. Ref: https://www.slimframework.com/docs/v4/concepts/value-objects.html");

        psr7Option.addEnum("slim-psr7", "Slim PSR-7 Message implementation")
                .addEnum("nyholm-psr7", "Nyholm PSR-7 Message implementation")
                .addEnum("guzzle-psr7", "Guzzle PSR-7 Message implementation")
                .addEnum("zend-diactoros", "Zend Diactoros PSR-7 Message implementation")
                .setDefault("slim-psr7");

        cliOptions.add(psr7Option);
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    public String getName() {
        return "php-lava-lumen";
    }

    @Override
    public String getHelp() {
        return "Generates a PHP Lava-lumen Framework server library(with Mock server).";
    }

    @Override
    public String apiFileFolder() {
        if(invokerPackage != "") {
            if (apiPackage.startsWith(invokerPackage + "\\")) {
                // need to strip out invokerPackage from path
    
                return (outputFolder + File.separator + toSrcPath(StringUtils.removeStart(apiPackage, invokerPackage + "\\"), srcBasePath) + File.separator + "Generated");
            }
           
            return (outputFolder + File.separator + toSrcPath(apiPackage, srcBasePath) + File.separator + "Generated");

        } else {
            if(!additionalProperties.containsKey("customPackageName")) {
                additionalProperties.put("customPackageName", "App");
            }
            return (outputFolder + File.separator + toSrcPath(this.getCamelizeOutputDir(apiPackage), srcBasePath) + File.separator + "Generated");
        }
        
    }

    @Override
    public String modelFileFolder() {
        if(invokerPackage != "") {
            if (modelPackage.startsWith(invokerPackage + "\\")) {
                // need to strip out invokerPackage from path
                return (outputFolder + File.separator + toSrcPath(StringUtils.removeStart(modelPackage, invokerPackage + "\\"), srcBasePath) + File.separator + "Generated");
            }
            return (outputFolder + File.separator + toSrcPath(modelPackage, srcBasePath) + File.separator + "Generated");
        } else {
            return (outputFolder + File.separator + toSrcPath(this.getCamelizeOutputDir(modelPackage), srcBasePath) + File.separator + "Generated");
        }
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (invokerPackage != "") {
            // Update the invokerPackage for the default authPackage
            authPackage = invokerPackage + "\\" + authDirName;
            // Update interfacesPackage
            interfacesPackage = invokerPackage + "\\" + interfacesDirName;
        } else {
            authPackage = "App" + "\\" + authDirName;
            interfacesPackage = "App" + "\\" + interfacesDirName;
        }

        additionalProperties.put("authPackage", authPackage + "\\" +"Generated");
        additionalProperties.put("authSrcPath", "./" + toSrcPath(authPackage, srcBasePath));

        // same for interfaces package
        additionalProperties.put("interfacesPackage", interfacesPackage);
        additionalProperties.put("interfacesSrcPath", "./" + toSrcPath(interfacesPackage, srcBasePath));
        additionalProperties.put("interfacesTestPath", "./" + toSrcPath(interfacesPackage, testBasePath));

        if (additionalProperties.containsKey(PSR7_IMPLEMENTATION)) {
            this.setPsr7Implementation((String) additionalProperties.get(PSR7_IMPLEMENTATION));
        }

        // reset implementation flags
        additionalProperties.put("isSlimPsr7", Boolean.FALSE);
        additionalProperties.put("isNyholmPsr7", Boolean.FALSE);
        additionalProperties.put("isGuzzlePsr7", Boolean.FALSE);
        additionalProperties.put("isZendDiactoros", Boolean.FALSE);

        // set specific PSR-7 implementation flag
        switch (getPsr7Implementation()) {
            case "slim-psr7":
                additionalProperties.put("isSlimPsr7", Boolean.TRUE);
                break;
            case "nyholm-psr7":
                additionalProperties.put("isNyholmPsr7", Boolean.TRUE);
                break;
            case "guzzle-psr7":
                additionalProperties.put("isGuzzlePsr7", Boolean.TRUE);
                break;
            case "zend-diactoros":
                additionalProperties.put("isZendDiactoros", Boolean.TRUE);
                break;
            default:
                LOGGER.warn(
                        "\"{}\" is invalid \"psr7Implementation\" codegen option. Default \"slim-psr7\" used instead.",
                        getPsr7Implementation());
                additionalProperties.put("isSlimPsr7", Boolean.TRUE);
        }

        if(invokerPackage != "") {
            // Add apigen.php in Routes/Generated folder
            supportingFiles.add(new SupportingFile("routes.mustache", "Routes/Generated", "apigen.php"));
        } else {
            // Add apigen.php in routes/Generated folder
            supportingFiles.add(new SupportingFile("routes.mustache", "routes/Generated", "apigen.php"));
        }
        
        // Add .gitignore to Generated folder
        // supportingFiles.add(new SupportingFile("gitignore", "Generated", ".gitignore"));
    }

    @Override
    public Map<String, Object> postProcessOperationsWithModels(Map<String, Object> objs, List<Object> allModels) {
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");
        addUserClassnameToOperations(operations);
        escapeMediaType(operationList);
        return objs;
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        Map<String, Object> apiInfo = (Map<String, Object>) objs.get("apiInfo");
        List<HashMap<String, Object>> apiList = (List<HashMap<String, Object>>) apiInfo.get("apis");
        for (HashMap<String, Object> api : apiList) {
            HashMap<String, Object> operations = (HashMap<String, Object>) api.get("operations");
            List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");

            // Sort operations to avoid static routes shadowing
            // ref: https://github.com/nikic/FastRoute/blob/master/src/DataGenerator/RegexBasedAbstract.php#L92-L101
            Collections.sort(operationList, new Comparator<CodegenOperation>() {
                @Override
                public int compare(CodegenOperation one, CodegenOperation another) {
                    if (one.getHasPathParams() && !another.getHasPathParams()) return 1;
                    if (!one.getHasPathParams() && another.getHasPathParams()) return -1;
                    return 0;
                }
            });
        }

        // generate authenticator only when hasAuthMethods === true
        if (objs.containsKey("hasAuthMethods") && Boolean.TRUE.equals(objs.get("hasAuthMethods"))) {
            if(invokerPackage != "") {
                supportingFiles.add(new SupportingFile("abstract_authenticator.mustache", toSrcPath(authPackage, srcBasePath) + File.separator + "Generated", toAbstractName("Authenticator") + ".php"));
            } else {
                supportingFiles.add(new SupportingFile("abstract_authenticator.mustache", toSrcPath(this.getCamelizeOutputDir(authPackage) , srcBasePath) + File.separator + "Generated", toAbstractName("Authenticator") + ".php"));
            }
        }
        return objs;
       
    }

    @Override
    public String toApiName(String name) {
        if (name.length() == 0) {
            // set notAbstartClassname for routes 
            additionalProperties.put("notAbstartClassname", "DefaultApi");
            return toAbstractName("DefaultApi");
        }
        // set notAbstartClassname for routes
        additionalProperties.put("notAbstartClassname", (camelize(name) + "Api"));
        additionalProperties.put("tagName", (name.toLowerCase(Locale.ROOT)));
        return toAbstractName(camelize(name) + "Api");
    }

    @Override
    public String toApiTestFilename(String name) {
        if (name.length() == 0) {
            return toAbstractName("DefaultApiTest");
        }
        return toAbstractName(camelize(name) + "ApiTest");
    }

    /**
     * Strips out abstract prefix and suffix from classname and puts it in "userClassname" property of operations object.
     *
     * @param operations codegen object with operations
     */
    private void addUserClassnameToOperations(Map<String, Object> operations) {
        String classname = (String) operations.get("classname");
        classname = classname.replaceAll("^" + abstractNamePrefix, "");
        classname = classname.replaceAll(abstractNameSuffix + "$", "");
        operations.put(USER_CLASSNAME_KEY, classname);
    }

    @Override
    public String encodePath(String input) {
        if (input == null) {
            return input;
        }

        // from DefaultCodegen.java
        // remove \t, \n, \r
        // replace \ with \\
        // replace " with \"
        // outer unescape to retain the original multi-byte characters
        // finally escalate characters avoiding code injection
        input = super.escapeUnsafeCharacters(
                StringEscapeUtils.unescapeJava(
                        StringEscapeUtils.escapeJava(input)
                                .replace("\\/", "/"))
                        .replaceAll("[\\t\\n\\r]", " ")
                        .replace("\\", "\\\\"));
        // .replace("\"", "\\\""));

        // from AbstractPhpCodegen.java
        // Trim the string to avoid leading and trailing spaces.
        input = input.trim();
        try {

            input = URLEncoder.encode(input, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%2F", "/")
                    .replaceAll("\\%7B", "{") // keep { part of complex placeholders
                    .replaceAll("\\%7D", "}") // } part
                    .replaceAll("\\%5B", "[") // [ part
                    .replaceAll("\\%5D", "]") // ] part
                    .replaceAll("\\%3A", ":") // : part
                    .replaceAll("\\%2B", "+") // + part
                    .replaceAll("\\%5C\\%5Cd", "\\\\d"); // \d part
        } catch (UnsupportedEncodingException e) {
            // continue
            LOGGER.error(e.getMessage(), e);
        }
        return input;
    }

    @Override
    public CodegenOperation fromOperation(String path,
                                          String httpMethod,
                                          Operation operation,
                                          List<Server> servers) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation, servers);
        op.path = encodePath(path);
        return op;
    }

    /**
     * Set PSR-7 implementation package.
     * Ref: https://www.slimframework.com/docs/v4/concepts/value-objects.html
     *
     * @param psr7Implementation PSR-7 implementation package
     */
    public void setPsr7Implementation(String psr7Implementation) {
        switch (psr7Implementation) {
            case "slim-psr7":
            case "nyholm-psr7":
            case "guzzle-psr7":
            case "zend-diactoros":
                this.psr7Implementation = psr7Implementation;
                break;
            default:
                this.psr7Implementation = "slim-psr7";
                LOGGER.warn("\"{}\" is invalid \"psr7Implementation\" argument. Default \"slim-psr7\" used instead.",
                        psr7Implementation);
        }
    }

    /**
     * Returns PSR-7 implementation package.
     *
     * @return PSR-7 implementation package
     */
    public String getPsr7Implementation() {
        return this.psr7Implementation;
    }

    public String getCamelizeOutputDir(String dirPath) {
        char c[] = dirPath.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }
}
