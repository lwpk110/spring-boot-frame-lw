package cn.tendata.mdcs.data.jpa.config;

import org.hibernate.cfg.ImprovedNamingStrategy;

@SuppressWarnings("serial")
public class CustomNamingStrategy extends ImprovedNamingStrategy {

	private static final String PLURAL_SUFFIX = "s";
	
//	public String tableName(String tableName) {
//        return tableName;
//    }
	
	public String columnName(String columnName) {
        return columnName;
    }
	
	/**
     * Transforms class names to table names by using the described naming conventions.
     * @param className
     * @return  The constructed table name.
     */
    @Override
    public String classToTableName(String className) {
        String tableNameInSingularForm = super.classToTableName(className);
        return transformToPluralForm(tableNameInSingularForm);
    }
 
    private String transformToPluralForm(String tableNameInSingularForm) {
        StringBuilder pluralForm = new StringBuilder();
 
        pluralForm.append(tableNameInSingularForm);
        if(!tableNameInSingularForm.endsWith(PLURAL_SUFFIX)){
            pluralForm.append(PLURAL_SUFFIX);
        }
        return pluralForm.toString();
    }
}
