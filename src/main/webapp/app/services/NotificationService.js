(function() {
  
  var launchIcon = "data:image/png;base64,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";
  var timeout = 7000;
  
  var service = angular.module("bacabs.services.notificationService", ['bacabs.services.webSocketService', 'bacabs.service.deploymentService']);
  
  service.factory('NotificationService', function(WebSocketService, DeploymentService, $q, $timeout) {
    return new (function() {
      var self = this;
      
      this.isPermitted = function() {
        return Notification.permission === 'granted' && localStorage.getItem("enableNotifications") == "true";
      };
      
      this.requestPermission = function() {
        var deferred = $q.defer();
        Notification.requestPermission(function(result) {
          localStorage.setItem("enableNotifications", (result === 'granted') ? 'true' : 'false');
          deferred.resolve(result === 'granted');
        });
        return deferred.promise;
      };
      
      this.stopNotifications = function() {
        localStorage.setItem('enableNotifications', 'false');
      };
      
      WebSocketService.registerListener("NewDeploymentEvent", function(event) {
        if (!self.isPermitted())
          return;
        var deployment = event.deployment;
        var notification = new Notification("New deployment", { body : deployment.identifier + " has been deployed", icon : launchIcon});
        notification.onclick = function() {
          window.open(notification.href);
        };
        
        $timeout(function() { notification.close(); }, timeout);
      });

      WebSocketService.registerListener("RemovedDeploymentEvent", function(event) {
        if (!self.isPermitted())
          return;
        var deployment = event.deployment;
        var notification = new Notification("Deployment removed", { body : deployment.identifier + " has been removed", icon : launchIcon});
        $timeout(function() { notification.close(); }, timeout);
      });

      WebSocketService.registerListener("UpdatedDeploymentEvent", function(event) {
        if (!self.isPermitted()) return;
        var deployment = event.deployment;
        var existingDeployment = DeploymentService.getDeployment(deployment.identifier);
        if (existingDeployment == null) return;
        if (!angular.isDefined(deployment.jiraIssue) || deployment.jiraIssue == null || !angular.isDefined(deployment.jiraIssue.acceptanceTaskStatusName)) return;
        if (angular.isDefined(existingDeployment.jiraIssue) && deployment.jiraIssue.acceptanceTaskStatusName == existingDeployment.jiraIssue.acceptanceTaskStatusName) return;

        var notification = new Notification("Deployment Status Change", { body : deployment.identifier + "'s acceptance task has transitioned to " + deployment.jiraIssue.acceptanceTaskStatusName, icon : launchIcon });
        $timeout(function() { notification.close(); }, timeout);
      });

    })();
  })
  
})();
