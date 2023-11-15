// websocket needs pinging and reconnection feature in case it fails
var lamsWebsockets = lamsWebsockets || {};

function isWebsocketClosed(instanceName) {
    let websocket = lamsWebsockets[instanceName];
    if (!websocket) {
        console.error("Websocket not initialized: " + instanceName);
        return true;
    }
    return websocket.instance.readyState == WebSocket.CLOSING
        || websocket.instance.readyState == WebSocket.CLOSED;
}

function websocketReconnect(instanceName) {
    let websocket = lamsWebsockets[instanceName];
    if (!websocket) {
        console.error("Websocket not initialized: " + instanceName);
        return;
    }
    if (websocket.reconnectAttempts < 20) {
        websocket.reconnectAttempts++;
        // maybe iPad went into sleep mode?
        // we need this websocket working, so init it again after delay
        console.log("Reconnecting to websocket in 3 seconds [attempt "
            + websocket.reconnectAttempts + "/20]: " + instanceName);
        setTimeout(function (){
            initWebsocket(instanceName);
        }, 3000);
    } else {
        console.error('Websocket reconnect failed, reloading page: ' + instanceName);
        alert('Connection issue. The page will now reload.');
        document.location.reload();
    }
}

function websocketPing(instanceName, skipPing){
    let websocket = lamsWebsockets[instanceName];
    if (!websocket) {
        console.error("Websocket not initialized: " + instanceName);
        return;
    }
    if (websocket.instance.readyState == WebSocket.CLOSING
        || websocket.instance.readyState == WebSocket.CLOSED){
        websocketReconnect(instanceName);
        return;
    }

    if (websocket.pingTimeout)  {
        clearTimeout(websocket.pingTimeout);
    }

    // check and ping every 3 minutes
    websocket.pingTimeout = setTimeout(function (){
        websocketPing(instanceName);
    }, 3*60*1000);

    // initial set up does not send ping
    if (!skipPing) {
        websocket.instance.send("ping");
    }
};

function initWebsocket(instanceName, url, onMessageFunction){
    let websocket = lamsWebsockets[instanceName];
    if (!websocket) {
        websocket = {
            initTime : null,
            url : null,
            instance : null,
            pingTimeout : null,
            reconnectAttempts : 0
        };
        lamsWebsockets[instanceName] = websocket;
    } else if (websocket.instance) {
        if (!onMessageFunction) {
            onMessageFunction = websocket.instance.onmessage;
        }
        try {
            websocket.instance.close(1000);
        } catch (e) {
            console.error(e);
        }
        websocket.instance = null;
    }

    if (url) {
        websocket.url = url;
    }

    websocket.initTime = Date.now();
    try {
        websocket.instance = new WebSocket(websocket.url);
    } catch (e) {
        console.error(e);
        websocketReconnect(instanceName);
        return null;
    }
    websocket.instance.onclose = function(e){
        // check reason and whether the close did not happen immediately after websocket creation
        // (possible access denied, user logged out?)
        if (e.code === 1006 && Date.now() - websocket.initTime > 1000) {
            websocketReconnect(instanceName);
        }
    };
    if (onMessageFunction) {
        websocket.instance.onmessage = onMessageFunction;
    }

    // set up timer for the first time
    websocketPing(instanceName, true);

    return websocket.instance;
}

function sendToWebsocket(instanceName, message) {
    let websocket = lamsWebsockets[instanceName];
    if (!websocket) {
        console.error("Websocket not initialized: " + instanceName);
        return;
    }
    if (websocket.instance.readyState == WebSocket.CLOSING
        || websocket.instance.readyState == WebSocket.CLOSED){
        websocketReconnect(instanceName);
        return;
    }
    websocket.instance.send(message);
}