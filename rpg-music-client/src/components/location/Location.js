import { CentredFlexColumn } from "../styled/Layouts";
import LocationButtons from "./LocationButtons";

const Location = ({ forwardMessage, gameState }) => {
    const sendLocationMessage = (location) => {
        const messageJSON = JSON.stringify({
            event: "SELECT_TUNE",
            tune: location
        });
        forwardMessage(messageJSON);
    }
    return (
        <CentredFlexColumn>
            <h3>Location</h3>
            <LocationButtons
                sendLocationMessage={sendLocationMessage}
            />
        </CentredFlexColumn>
    );
}

export default Location;