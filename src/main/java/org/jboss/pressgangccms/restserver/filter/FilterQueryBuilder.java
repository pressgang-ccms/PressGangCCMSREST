package org.jboss.pressgangccms.restserver.filter;

public interface FilterQueryBuilder
{
	String getMatchTagString(Integer tagId);
	String getNotMatchTagString(Integer tagId);
	String getMatchingLocalString(String locale);
	String getNotMatchingLocalString(String locale);
	void processFilterString(String fieldName, String fieldValue);
	String getFilterString();
	String getSelectAllQuery();
}
