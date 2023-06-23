import { TransportButton } from "../styled/Buttons";
import { ThreeColGrid } from "../styled/Layouts";

const TransportButtons = ({ sendTransportMessage, sendNavMessage, isPlaying }) => {

    return (

        <ThreeColGrid>
            <TransportButton onClick={() => sendTransportMessage("STOP")}>Stop</TransportButton>
            <TransportButton onClick={() => sendNavMessage("NEXT_SECTION")}>Next Section</TransportButton>
            <TransportButton onClick={() => sendTransportMessage("PLAY")}>Play</TransportButton>
        </ThreeColGrid>

    );
}

export default TransportButtons;