(function() {

  var WebSocketService = function($timeout) {
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
      for (var i = 0; i < topicListeners.length; i++) {
        var topicInfo = topicListeners[i];
        if (topic.search(topicInfo.regex) === 0) {
          for (var j = 0; j < topicInfo.listeners.length; j++) {
            notifyListener(listeners[j], data);
          }
        }
      }
    };

    var notifyListener = function(listener, data) {
      $timeout(function() { listener(data); });
    }

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
  };
  
  angular.module('bacabs.services.webSocketService', [])
      .service('WebSocketService', WebSocketService);
})();