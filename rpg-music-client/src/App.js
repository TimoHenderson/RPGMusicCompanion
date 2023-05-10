import './App.css';

import { useState, useEffect } from 'react';
import Controls from './containers/Controls';

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
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        gap: "2rem",
      }}>
      <h1>RPG Music Companion</h1>
      <Controls sendMessage={sendMessage} />
    </div>
  );
}

{/* <button onClick={() => handleButtonClick('hello')}>Send Hello</button>
<button onClick={() => handleButtonClick('world')}>Send World</button>
<button>{socket ? "Connected" : "Connect"}</button> */}
export default App;

