
import './App.css';

import { useState, useEffect } from 'react';
// import XYpad from './XYpad';

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

  const connectToSocket = () => {



  }

  const handleButtonClick = (message) => {
    if (socket) {
      const messageJSON = JSON.stringify({
        message
      });
      socket.send(messageJSON);
    }
  };

  return (
    <div>
      {/* <XYpad socket={socket} /> */}
      <button onClick={() => handleButtonClick('hello')}>Send Hello</button>
      <button onClick={() => handleButtonClick('world')}>Send World</button>
      <button onClick={() => connectToSocket()}>{socket ? "Connected" : "Connect"}</button>
    </div>
  );
}

export default App;

