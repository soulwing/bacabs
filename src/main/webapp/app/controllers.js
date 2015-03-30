(function() {

  var controllers = angular.module('bacabs.controllers',
      ['bacabs.services', 'bacabs.directives']);

  controllers.controller('appController', function($scope, deployments, NotificationService, WebSocketService) {
    $scope.deployments = deployments;
    window.deployments = deployments;
    $scope.allowDesktopNotifications = NotificationService.isPermitted();

    $scope.requestNotifications = function() {
      NotificationService.requestPermission().then(function(granted) {
        $scope.allowDesktopNotifications = granted;
      });
    };

    $scope.stopNotifications = function() {
      NotificationService.stopNotifications();
      $scope.allowDesktopNotifications = false;
    };
  });

})();
