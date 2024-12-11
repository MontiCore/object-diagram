<#-- (c) https://github.com/MontiCore/monticore -->
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