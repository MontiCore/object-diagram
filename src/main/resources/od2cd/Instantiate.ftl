<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("type", "attributes", "values", "objectname")}
${cd4c.method("public ${type} instantiate${objectname?capFirst}()")}

return Mill.${type?uncapFirst}Builder()
<#list attributes as attribute>
  .set${attribute?capFirst}(${values[attribute?index]})
</#list>
.build();