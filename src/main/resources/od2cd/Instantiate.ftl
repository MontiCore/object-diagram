<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("type", "attributes", "values")}
${cd4c.method("public ${type} inst()")}

return ${type}Builder()
<#list attributes as attribute>
  .set${attribute?capFirst}(${values[attributes?index]})
</#list>
;