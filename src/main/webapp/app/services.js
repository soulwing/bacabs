(function() {
  
  var otherServices = [
     'bacabs.services.cacheService',
     'bacabs.services.deploymentService',
     'bacabs.services.webSocketService',
     'bacabs.services.notificationService'
  ];
  
  var services = angular.module('bacabs.services', otherServices);

})();
