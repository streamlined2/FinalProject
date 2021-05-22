package edu.practice.finalproject.controller.transition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.form.Form;

public class TransitionRuleMap {
	
	private static class TransitionRuleKey {
		private final User user;
		private final Form form;
		private final Action action;
		
		private TransitionRuleKey(User user,Form form,Action action){
			this.user=user;
			this.form=form;
			this.action=action;
		}
		
		@Override public boolean equals(final Object o) {
			if(o instanceof TransitionRuleKey) {
				final TransitionRuleKey key=(TransitionRuleKey)o;
				return 
						Objects.equals(user, key.user) &&
						Objects.equals(form, key.form) &&
						Objects.equals(action, key.action);
			}
			return false;
		}
		
		@Override public int hashCode() {
			return Objects.hash(user,form,action);
		}
	}
	
	private final Map<TransitionRuleKey,Form> ruleMap=new HashMap<>();
	
	public void addRule(User user,Form sourceForm,Action action,Form targetForm) {
		ruleMap.put(new TransitionRuleKey(user,sourceForm,action), targetForm);
	}
	
	public Form getNextForm(User user,Form sourceForm,Action action) {
		return ruleMap.get(new TransitionRuleKey(user, sourceForm, action));
	}

}
