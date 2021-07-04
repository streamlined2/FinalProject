package edu.practice.finalproject.view.tag;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.practice.finalproject.utilities.Utils;

public class OptionSelectorTag extends SimpleTagSupport {
	private static final String VALUES_PARAMETER_SHOULD_CONTAIN_AT_LEAST_1_ITEM = "option.selector.tag.values-contain-one-item";

	private String name;
	private String[] values;
	private String selectedValue;
	private boolean required = false;

	public String getName() { return name;}
	public void setName(String name) { this.name = name;}
	
	public String[] getValues() { return values;}
	public void setValues(String[] values) { this.values = values;}
	
	public String getSelectedValue() { return selectedValue;}
	public void setSelectedValue(String selectedValue) { this.selectedValue = selectedValue;}
	
	public boolean getRequired() { return required;}
	public void setRequired(boolean required) { this.required = required;}

	private StringBuilder option(String item, boolean selected) {
		StringBuilder sb = new StringBuilder("<option value=\"").append(item).append("\"");
		if(selected) sb.append(" selected");
		sb.append(">").append(item).append("</option>");
		return sb;
	}
	
	private StringBuilder selectStart(String name, boolean required) {
		StringBuilder sb = new StringBuilder().
				append("<select name=\"").append(name).append("\" id=\"").append(name).append("\"")	;
		if(required) sb.append(" required");
		sb.append(">");
		return sb;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		if(values.length<1) throw new JspException(Utils.message(VALUES_PARAMETER_SHOULD_CONTAIN_AT_LEAST_1_ITEM));
		selectedValue = Objects.requireNonNullElse(selectedValue, values[0]);
		
		JspWriter out = getJspContext().getOut();
		out.append(selectStart(name,required));
		for(String value:values) {
			out.append(option(value, value.equals(selectedValue)));
		}
		out.append("</select>");
	}
}
