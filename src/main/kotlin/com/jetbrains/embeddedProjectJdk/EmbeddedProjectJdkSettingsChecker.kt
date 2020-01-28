package com.jetbrains.embeddedProjectJdk

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.MessageType

class EmbeddedProjectJdkSettingsChecker : StartupActivity {
  companion object {
    val PER_PROJECT_JDK_SETTINGS = NotificationGroup.toolWindowGroup("Per Project JDK Settings", "PerProjectJdkSettings")
  }

  private val myLogger = Logger.getInstance(EmbeddedProjectJdkSettingsChecker::class.java)

  override fun runActivity(project: Project) {
    myLogger.debug("Handle before project loaded event")
    if (JdkUtil.hasDifferentJdkSettings(project)) {
      PER_PROJECT_JDK_SETTINGS
        .createNotification("You have different JDK settings for project", MessageType.WARNING)
        .addAction(object : NotificationAction(PluginBundle.message("notification.action.text")) {
          override fun actionPerformed(e: AnActionEvent, notification: Notification) {
            ActionManager.getInstance().getAction("LoadJdkSettingsFromProject").actionPerformed(e)
            notification.expire()
          }
        }).notify(project)
    }
  }
}
