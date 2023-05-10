import XYPad from '../components/XYPad';
import Navigation from '../components/navigation/Navigation';
import Transport from '../components/transport/Transport';
import Location from '../components/location/Location';

const Controls = ({ sendMessage }) => {

    const forwardMessage = (JSONString) => {
        sendMessage(JSONString);
    }

    return (
        <div
            style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr  ",
                alignItems: "center",
                justifyContent: "start",
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
                <Location forwardMessage={forwardMessage} />
                <Navigation forwardMessage={forwardMessage} />
                <Transport forwardMessage={forwardMessage} />
            </div>
        </div>
    );
}

export default Controls;