package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.services.MailNotificationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotifyModeratorDelegate implements JavaDelegate {

    private final MailNotificationService mailNotificationService;

    @Override
    public void execute(DelegateExecution execution) {
        App app = (App) execution.getVariable("app");
        String jiraIssueId = (String) execution.getVariable("jiraIssueId");
        mailNotificationService.notifyModeratorOfNewTask(app.getName(), app.getId(), jiraIssueId);
    }
}
