import XYPad from '../components/XYPad';
import Transport from '../components/transport/Transport';
import Location from '../components/location/Location';
import { ControlPanel } from '../components/styled/Layouts';

const Controls = ({ sendMessage, gameState }) => {

    const forwardMessage = (JSONString) => {
        sendMessage(JSONString);
    }

    return (
        <ControlPanel>
            <XYPad forwardMessage={forwardMessage} />
            <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center",
                    gap: "2rem",
                }}>
                <Location forwardMessage={forwardMessage} gameState={gameState} />
                <Transport forwardMessage={forwardMessage} gameState={gameState} />
            </div>
        </ControlPanel>
    );
}

export default Controls;