package edu.practice.finalproject.view.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.utilities.Utils;

public class TableViewTag extends SimpleTagSupport {
	
	private Map<String,String> buttons;
	private String[] queryHeader;
	private List<Object[]> queryData;
	private String order;

	public Map<String, String> getButtons() { return buttons;}
	public void setButtons(Map<String, String> buttons) { this.buttons = buttons;}

	public String[] getQueryHeader() { return queryHeader;	}
	public void setQueryHeader(String[] queryHeader) { this.queryHeader = queryHeader;}

	public List<Object[]> getQueryData() { return queryData;}
	public void setQueryData(List<Object[]> queryData) {	this.queryData = queryData;}

	public String getOrder() { return order;}
	public void setOrder(String order) { this.order = order;}

	private StringBuilder head() {
		StringBuilder sb=new StringBuilder();
		sb.append("<thead>").append("<tr>");
		for(Entry<String, String> button:buttons.entrySet()) {
			sb.append("<th>&nbsp;</th>");
		}
		for(String header:queryHeader) {
			sb.append("<th>").append(Utils.message("table-view.header."+header.replace(" ", "-"))).append("</th>");
		}
		sb.append("</tr>").append("</thead>");
		return sb;
	}
	
	private StringBuilder body() {		
		StringBuilder sb = new StringBuilder();
		sb.append("<tbody>");
		int num=0;
		for(Object[] row:queryData) {
			sb.append("<tr>");
			for(Entry<String, String> button:buttons.entrySet()) {
				sb.append("<td>");
				sb.
					append("<input type=\"submit\" value=\"").
					append(Utils.message("table-view.button."+button.getKey())).
					append("\" formaction=\"").
					append(FCServlet.MAPPING_PATTERN).
					append("?action=").
					append(button.getKey()).
					append("&").
					append(order).
					append("=").
					append(num).
					append("\" formmethod=\"post\"/>");
				sb.append("</td>");
			}
			for(Object value:row) {
				sb.append("<td>").append(value).append("</td>");
			}
			sb.append("</tr>");
			num++;
		}
		sb.append("</tbody>");
		return sb;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.append("<table>");
		out.append(head());
		out.append(body());
		out.append("</table>");
	}
}
