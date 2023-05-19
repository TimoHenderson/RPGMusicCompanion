import './App.css';

import { useState, useEffect } from 'react';
import Controls from './containers/Controls';
import Header from './components/header/Header';

function App() {
  const [socket, setSocket] = useState(null);
  const [connected, setConnected] = useState(false);
  const [connectClicked, setConnectClicked] = useState(false);
  const [gameState, setGameState] = useState({
    isPlaying: false,
    currentTune: null,
    prevTune: null,
    currentMovement: null,
    nextMovement: null,
    currentSection: null,
    nextSection: null
  });
  const [availableLocations,setAvailableLocations] =useState([]);


  useEffect(() => {
    const ws = new WebSocket('ws://localhost:8080');
    setSocket(ws);



    ws.addEventListener('open', () => {
      console.log('Connected to server');
      setConnected(true);
    });

    ws.addEventListener('close', () => {
      console.log('Disconnected from server');
      setConnected(false);
    });

    return () => {
      ws.close();
    };

  }, [connectClicked]);

  const reconnect = () => {
    if (!connected) {
      setConnectClicked(!connectClicked);
    }
  }

  const sendMessage = (message) => {
    if (socket) {
      socket.send(message);
    }
  };
  if (socket) {
    socket.addEventListener('message', (event) => {
      console.log(`Received message: ${event.data}`);
      const message = JSON.parse(event.data);
      receiveMessage(message)

    });
  }
  const receiveMessage = (message) => {
    if (message["event"] === 'gameState') {
      console.log("received game state")
      setGameState({ ...gameState, ...message });
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
      <Header connected={connected} reconnect={reconnect} />
      <Controls sendMessage={sendMessage} gameState={gameState} />
    </div>
  );
}

export default App;

