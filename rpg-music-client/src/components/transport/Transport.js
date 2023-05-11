import TransportButtons from "./TransportButtons";

const Transport = ({ forwardMessage, gameState }) => {

    const sendTransportMessage = (action) => {
        const messageJSON = JSON.stringify({
            event: "TRANSPORT",
            action
        });
        forwardMessage(messageJSON);
    }
    if (!gameState) {
        return null;
    }
    return (
        <div
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "start",
            }}>
            <TransportButtons
                sendTransportMessage={sendTransportMessage}
                isPlaying={gameState.isPlaying}

            />
        </div>
    );
}

export default Transport;