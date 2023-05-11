const TransportButtons = ({ sendTransportMessage, isPlaying }) => {

    return (
        <div
            style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr 1fr ",
                gap: "1rem",
                justifyContent: "center",
            }}>
            {isPlaying ?
                <button onClick={() => sendTransportMessage("STOP")}>Stop</button> :
                <button onClick={() => sendTransportMessage("PLAY")}>Play</button>
            }
        </div>
    );
}

export default TransportButtons;