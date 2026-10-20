<#-- (c) https://github.com/MontiCore/monticore -->
<#-- @ftlvariable name="tc" type="de.monticore.generating.templateengine.TemplateController" -->
<#-- @ftlvariable name="cd4c" type="de.monticore.cd.methodtemplates.CD4C" -->
<#-- @ftlvariable name="cp" type="de.monticore.od2cd.CompositionPrinter" -->
<#-- @ftlvariable name="types" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="names" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="links" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="objects" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="odname" type="java.lang.String" -->
${tc.signature("types", "names", "links", "objects", "odname")}
${cd4c.method("public " + odname + "ODInstances instantiate()")}

${odname}ODInstances _inst = new ${odname}ODInstances();
<#list objects as object>
  ${types[object?index]} ${names[object?index]} = instantiate${object?capFirst}();
</#list>
<#list links as link>
  ${link};
</#list>
List<Object> objects = new ArrayList<>();
<#list objects as object>
  //# objects.add(...)
  _inst.set${object?capFirst}((${types[object?index]})${object}${cp.write(types[object?index])});
</#list>
return _inst;