(function() {
  
  var services = angular.module('bacabs.services.webSocketService', []);
  
  services.factory('WebSocketService', function($q, $http, $interval) {
    return new (function() {
      var ws = new WebSocket("ws://" + window.location.host + CONTEXT_BASE + "/websocket");
      
      ws.onopen = function() {
        console.log("Websocket is opened");
      };
      
      ws.onclose = function() {
        console.log("Websocket has closed");
      };
      
      ws.onmessage = function(event) {
        var data = JSON.parse(event.data);
        console.log("Websocket received a message: ", angular.copy(data));

        var topic = data.type;
        for (var topicListenerKey in topicListeners) {
          var topicInfo = topicListeners[topicListenerKey];
          if (topic.search(topicInfo.regex) === 0) {
            for (var callbackKey in topicInfo.listeners) {
              topicInfo.listeners[callbackKey](data);
            }
          }
        }
      };
      
      var topicListeners = {};
      
      this.registerListener = function(topic, callback) {
        if (typeof topicListeners[topic] === 'undefined') {
          topicListeners[topic] = {
            listeners : Array(),
            regex : new RegExp("^" + topic)
          };
        }
        topicListeners[topic].listeners.push(callback);
      };
      
    });
  });
  
})();