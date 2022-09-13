<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("instantiators", "types", "names", "links")}
${cd4c.method("public List<Object> instantiate()")}

<#list instantiators as instantiator>
  ${types[instantiator?index]} ${names[instantiator?index]} = ${instantiator?uncapFirst}.instantiate();
</#list>
<#list links as link>
  ${link}
</#list>
List<Object> objects = new ArrayList<>();
<#list instantiators as instantiator>
  objects.add(${names[instantiator?index]});
</#list>
return objects;