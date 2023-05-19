import XYPad from '../components/XYPad';
import Navigation from '../components/navigation/Navigation';
import Transport from '../components/transport/Transport';
import Location from '../components/location/Location';

const Controls = ({ sendMessage, gameState }) => {

    const forwardMessage = (JSONString) => {
        sendMessage(JSONString);
    }

    return (
        <div
            style={{
                display: "flex",
                flexWrap: "wrap",
                alignItems: "center",
                justifyContent: "center",
                gap: "2rem",
            }}>
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
                <Navigation forwardMessage={forwardMessage} gameState={gameState} />
                <Transport forwardMessage={forwardMessage} gameState={gameState} />
            </div>
        </div>
    );
}

export default Controls;