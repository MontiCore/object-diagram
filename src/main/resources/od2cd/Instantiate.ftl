<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("type", "printInfo", "attributes", "values", "objectname")}
${cd4c.method("public ${printInfo} instantiate${objectname?capFirst}()")}

return ${cp.create(type)}
<#list attributes as attribute>
  ${cp.update(attribute, values[attribute?index])}
</#list>
;
