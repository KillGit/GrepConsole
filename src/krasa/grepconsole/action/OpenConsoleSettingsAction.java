package krasa.grepconsole.action;

import javax.swing.*;

import krasa.grepconsole.gui.SettingsContext;
import krasa.grepconsole.plugin.MyConfigurable;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;

public class OpenConsoleSettingsAction extends HighlightManipulationAction {
	public static final Icon ICON = IconLoader.getIcon("highlight.gif", OpenConsoleSettingsAction.class);
	private ConsoleView console;

	public OpenConsoleSettingsAction() {
	}

	public OpenConsoleSettingsAction(ConsoleView console) {
		super("Open Grep Console settings", null, ICON);
		this.console = console;
	}

	@Override
	public void actionPerformed(final AnActionEvent e) {
		Project project = e.getProject();
		actionPerformed(project, SettingsContext.NONE);
	}

	public void actionPerformed(Project project, SettingsContext console) {
		MyConfigurable instance = new MyConfigurable();
		instance.setCurrentAction(this);
		instance.prepareForm(console);
		ShowSettingsUtil.getInstance().editConfigurable(project, "GrepConsoleSettings", instance, true);
		instance.setCurrentAction(null);
	}

	@Override
	public void applySettings() {
		if (console != null) {
			resetHighlights(console);
		}
	}
}
