<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("types", "names", "links", "objects")}
${cd4c.method("public List<Object> instantiate()")}

<#list objects as object>
  ${types[object?index]} ${names[object?index]} = instantiate${object?capFirst}();
</#list>
<#list links as link>
  ${link};
</#list>
List<Object> objects = new ArrayList<>();
<#list objects as object>
  objects.add(${object}${cp.write(types[object?index])});
</#list>
return objects;