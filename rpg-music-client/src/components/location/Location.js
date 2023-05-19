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
        <div
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "start",
            }}>
            <LocationButtons
                sendLocationMessage={sendLocationMessage}
            />
        </div>
    );
}

export default Location;