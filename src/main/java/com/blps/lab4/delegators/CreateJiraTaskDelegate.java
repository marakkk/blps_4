package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.resourceAdapter.*;
import jakarta.resource.ResourceException;
import jakarta.resource.cci.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("createJiraTaskDelegate")
public class CreateJiraTaskDelegate implements JavaDelegate {

    private final ConnectionFactory jiraConnectionFactory;
    private final AppRepository appRepository;

    @Override
    public void execute(DelegateExecution execution) {
        Long appId = (Long) execution.getVariable("appId");
        App app = appRepository.findById(appId).orElseThrow();

        try (JiraConnection jiraConnection = (JiraConnection) jiraConnectionFactory.getConnection()) {
            JiraInteraction interaction = (JiraInteraction) jiraConnection.createInteraction();
            JiraResponseRecord response = (JiraResponseRecord) interaction.execute(new JiraRequestRecord(
                    app.getName(),
                    app.getId(),
                    "createTask",
                    "Create review task for app: " + app.getName()
            ));
            interaction.execute(new JiraStatusUpdateRecord(response.getIssueId(), "In Progress", "updateStatus"));

            execution.setVariable("jiraIssueId", response.getIssueId());
        } catch (ResourceException e) {
            throw new RuntimeException("Failed to connect to Jira", e);
        }
    }
}
