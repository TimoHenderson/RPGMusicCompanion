import NavButtons from "./NavButtons";

const Navigation = ({ forwardMessage }) => {

    const sendNavMessage = (action) => {
        const messageJSON = JSON.stringify({
            event: "NAVIGATION",
            action
        });
        forwardMessage(messageJSON);
    }

    return (
        <NavButtons sendNavMessage={sendNavMessage} />
    );
}

export default Navigation;