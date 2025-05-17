package com.blps.lab4.delegators;

import com.blps.lab4.entities.googleplay.App;
import com.blps.lab4.enums.AppStatus;
import com.blps.lab4.repo.googleplay.AppRepository;
import com.blps.lab4.resourceAdapter.JiraConnection;
import com.blps.lab4.resourceAdapter.JiraInteraction;
import com.blps.lab4.resourceAdapter.JiraStatusUpdateRecord;
import jakarta.resource.ResourceException;
import jakarta.resource.cci.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UpdateAppStatusDelegate implements JavaDelegate {
    private final AppRepository appRepository;
    private final ConnectionFactory jiraConnectionFactory;

    @Override
    public void execute(DelegateExecution execution) {
        Boolean approved = (Boolean) execution.getVariable("approved");
        String moderatorComment = (String) execution.getVariable("moderatorComment");
        App app = (App) execution.getVariable("app");
        String jiraIssueId = (String) execution.getVariable("jiraIssueId");

        try (JiraConnection jiraConnection = (JiraConnection) jiraConnectionFactory.getConnection()) {
            JiraInteraction interaction = (JiraInteraction) jiraConnection.createInteraction();

            if (approved) {
                app.setStatus(AppStatus.APPROVED);
                interaction.execute(new JiraStatusUpdateRecord(jiraIssueId, "Done", "updateStatus"));
                execution.setVariable("message", "App approved by moderator.");
            } else {
                app.setStatus(AppStatus.REJECTED);
                interaction.execute(new JiraStatusUpdateRecord(jiraIssueId, "Rejected", "updateStatus"));
                execution.setVariable("reason", moderatorComment);
            }

            appRepository.save(app);

        } catch (ResourceException e) {
            throw new RuntimeException("Failed to connect to Jira", e);
        }
    }
}
