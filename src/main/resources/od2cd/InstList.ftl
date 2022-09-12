<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("instantiators")}
${cd4c.method("public List<Object> inst()")}

List<\Object> objects = new ArrayList<>();
<#list instantiators as instantiator>
objects.add(${instantiator}.inst());
</#list>
return objects;