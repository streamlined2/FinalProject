package edu.practice.finalproject.view.form;

import java.util.Map;
import java.util.Objects;

import edu.practice.finalproject.controller.FCServlet;
import edu.practice.finalproject.controller.transition.FormDispatcher;
import edu.practice.finalproject.view.action.Action;

/**
 * Abstract form class to support mapping of action parameters to action objects
 * @author Serhii Pylypenko
 *
 */
public abstract class ActionMapForm extends Form {
	
	protected ActionMapForm(String name) {
		super(name);
	}
	
	protected static class ParameterValue {		
		private final String name;
		private final String value;
		
		protected ParameterValue(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		@Override public int hashCode() { return Objects.hash(name,value);}
		@Override public boolean equals(Object o) {
			if(o instanceof ParameterValue) return name.equals(((ParameterValue)o).name) && value.equals(((ParameterValue)o).value);
			return false;
		}
	}
	
	protected Map<String,Action> getActionMap(){ return Map.of();}
	protected Map<ParameterValue,Action> getParameterValueMap(){ return Map.of();}

	/**
	 * Action search method revamped as template method to seek in provided parameter/action map
	 * @param parameters map of parameter name and values 
	 */
	@Override
	public Action getAction(Map<String, String[]> parameters) {
		for(Map.Entry<String,Action> entry:getActionMap().entrySet()) {
			if(FCServlet.isActionPresent(parameters,entry.getKey())) return entry.getValue();
		}
		for(Map.Entry<ParameterValue,Action> entry:getParameterValueMap().entrySet()) {
			if(FCServlet.ifParameterEquals(parameters,entry.getKey().name,entry.getKey().value)) return entry.getValue();
		}
		return super.getAction(parameters);
	}

	@Override
	public Action getDefaultAction() {
		return FormDispatcher.BACK_ACTION;
	} 
}
