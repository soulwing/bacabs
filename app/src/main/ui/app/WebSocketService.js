(function() {

  angular.module('bacabs')
      .service('WebSocketService', WebSocketService)
      .run(_initService);

  function WebSocketService($timeout, EventBus) {
    var topicListeners = {};
    _init();
    return {
      registerListener : registerListener
    };

    ////////

    function _init() {
      var protocol = (window.location.protocol.indexOf('https') === 0) ? "wss" : "ws";
      var ws = new WebSocket(protocol + "://" + window.location.host + "/websocket");
      ws.onopen = _onOpen;
      ws.onclose = _onClose;
      ws.onmessage = _onMessage;
    }

    /**
     * @ngdoc method
     * @name WebSocketService#registerListener
     * @param topic {string} The name of the topic to register to. Can be a regex
     * @param callback {function} A callback function
     *
     * @description
     * Register a listener for a WebSocket event
     */
    function registerListener(topic, callback) {
      if (typeof topicListeners[topic] === 'undefined') {
        topicListeners[topic] = {
          listeners : Array(),
          regex : new RegExp("^" + topic)
        };
      }
      topicListeners[topic].listeners.push(callback);
    };

    /**
     * WebSocket onopen listener
     * @private
     */
    function _onOpen() {
      console.log("Websocket is opened");
    }

    /**
     * WebSocket onclose listener
     * @private
     */
    function _onClose() {
      console.log("Websocket has closed");
    }

    /**
     * WebSocket onmessage listener. Finds registered listeners and notifies
     * them
     * @private
     */
    function _onMessage(event) {
      var data = JSON.parse(event.data);
      console.log("Websocket received a message: ", angular.copy(data));

      var topic = data.type;
      for (var topicKey in topicListeners) {
        var topicInfo = topicListeners[topicKey];
        if (topic.search(topicInfo.regex) === 0) {
          for (var j = 0; j < topicInfo.listeners.length; j++) {
            _notifyListener(topicInfo.listeners[j], data);
          }
        }
      }

      $timeout(function() { EventBus.broadcast(topic, data); });
    }

    /**
     * Listener notification helper.
     * @param listener
     * @param data
     * @private
     */
    function _notifyListener(listener, data) {
      $timeout(function() { listener(data); });
    }

  };

  function _initService(WebSocketService) {
    // Doesn't need to do anything. Just ensures that the service starts
  }
})();