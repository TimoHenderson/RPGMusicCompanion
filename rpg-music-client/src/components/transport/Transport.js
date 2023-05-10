import TransportButtons from "./TransportButtons";

const Transport = ({ forwardMessage }) => {

    const sendTransportMessage = (action) => {
        const messageJSON = JSON.stringify({
            event: "TRANSPORT",
            action
        });
        forwardMessage(messageJSON);
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
            />
        </div>
    );
}

export default Transport;