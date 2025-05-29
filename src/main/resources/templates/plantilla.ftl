public class ${className}DTO {
    <#list columns as column>
        private ${column.type} ${column.name};
    </#list>

    public ${className}DTO() {}

    public ${className}DTO(<#list columns as column>${column.type} ${column.name}<#if column_has_next>,</#if> </#list>) {
        <#list columns as column>
            this.${column.name} = ${column.name};
        </#list>
    }

    <#list columns as column>
        public ${column.type} get${column.name?cap_first}() {
            return ${column.name};
        }

        public void set${column.name?cap_first}(${column.type} ${column.name}) {
            this.${column.name} = ${column.name};
        }

    </#list>

    @Override
    public String toString() {
    return <#list columns as column>
        "${column.name}=" + get${column.name?cap_first}()<#if column_has_next> + </#if>
    </#list>;
    }
}