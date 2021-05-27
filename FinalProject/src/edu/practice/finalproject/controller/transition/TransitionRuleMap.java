package edu.practice.finalproject.controller.transition;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import edu.practice.finalproject.controller.admin.User;
import edu.practice.finalproject.view.action.Action;
import edu.practice.finalproject.view.form.Form;

public class TransitionRuleMap {
	
	private static class TransitionRuleKey {
		private final Class<? extends User> userClass;
		private final Form form;
		private final Action action;
		
		private TransitionRuleKey(final Class<? extends User> userClass,final Form form,final Action action){
			this.userClass=userClass;
			this.form=form;
			this.action=action;
		}
		
		@Override public boolean equals(final Object o) {
			if(o instanceof TransitionRuleKey) {
				final TransitionRuleKey key=(TransitionRuleKey)o;
				return 
						Objects.equals(userClass, key.userClass) &&
						Objects.equals(form, key.form) &&
						Objects.equals(action, key.action);
			}
			return false;
		}
		
		@Override public int hashCode() {
			return Objects.hash(userClass,form,action);
		}
		
		public boolean encompasses(final TransitionRuleKey key) {
			boolean suits=true;
			if(userClass!=null)	suits = suits && userClass.isAssignableFrom(key.userClass); 
			if(form!=null) suits = suits && Objects.equals(form, key.form);
			if(action!=null) suits = suits && Objects.equals(action, key.action); 
			return suits;
		}
	}
	
	private final Map<TransitionRuleKey,Form> ruleMap=new LinkedHashMap<>();
	
	public void addRule(Class<? extends User> userClass,Form sourceForm,Action action,Form targetForm) {
		ruleMap.put(new TransitionRuleKey(userClass,sourceForm,action), targetForm);
	}
	
	public Optional<Form> getNextForm(final User user,final Form sourceForm,final Action action) {
		final TransitionRuleKey seekKey=new TransitionRuleKey(user==null?null:user.getClass(), sourceForm, action);
		for(final Entry<TransitionRuleKey,Form> entry:ruleMap.entrySet()) {
			if(entry.getKey().encompasses(seekKey)) return Optional.of(entry.getValue());
		}
		return Optional.empty();
	}

}
