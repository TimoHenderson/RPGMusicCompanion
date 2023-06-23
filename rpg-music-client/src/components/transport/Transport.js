import { CentredFlexColumn } from "../styled/Layouts";
import TransportButtons from "./TransportButtons";

const Transport = ({ forwardMessage, gameState }) => {

    const sendTransportMessage = (action) => {
        const messageJSON = JSON.stringify({
            event: "TRANSPORT",
            action
        });
        forwardMessage(messageJSON);
    }

    const sendNavMessage = (action) => {
        const messageJSON = JSON.stringify({
            event: "NAVIGATION",
            action
        });
        forwardMessage(messageJSON);
    }

    if (!gameState) {
        return null;
    }
    return (
        <CentredFlexColumn>
            <h3>Transport</h3>
            <TransportButtons
                sendTransportMessage={sendTransportMessage}
                sendNavMessage={sendNavMessage}
                isPlaying={gameState.isPlaying}

            />
        </CentredFlexColumn>
    );
}

export default Transport;