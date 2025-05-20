package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.services.MailNotificationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("notifyModeratorDelegate")
public class NotifyModeratorDelegate implements JavaDelegate {

    private final MailNotificationService mailNotificationService;
    private final AppRepository appRepository;

    @Override
    public void execute(DelegateExecution execution) {
        Long appId = (Long) execution.getVariable("appId");
        App app = appRepository.findById(appId).orElseThrow();
        String jiraIssueId = (String) execution.getVariable("jiraIssueId");
        mailNotificationService.notifyModeratorOfNewTask(app.getName(), app.getId(), jiraIssueId);
    }
}
