
import './App.css';

import { useState, useEffect } from 'react';
import XYPad from './components/XYPad';

function App() {
  const [socket, setSocket] = useState(null);

  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8080');
    setSocket(ws);

    ws.addEventListener('message', (event) => {
      console.log(`Received message: ${event.data}`);
    });
    return () => {
      ws.close();
    };
  }, []);

  const sendMessage = (message) => {
    if (socket) {
      socket.send(message);
    }
  };



  const handleButtonClick = (message) => {
    if (socket) {
      const messageJSON = JSON.stringify({
        message
      });
      sendMessage(messageJSON);
    }
  };

  return (
    <div>
      {/* <XYpad socket={socket} /> */}
      <button onClick={() => handleButtonClick('hello')}>Send Hello</button>
      <button onClick={() => handleButtonClick('world')}>Send World</button>
      <button>{socket ? "Connected" : "Connect"}</button>
      <XYPad sendMessage={sendMessage} />

    </div>
  );
}

export default App;

