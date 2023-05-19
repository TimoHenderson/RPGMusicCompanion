const Header = ({ connected, reconnect }) => {
    return (
        <div style={{
            width: "100%",
            background: "darkgray",
            display: "grid",
            justifyContent: "center",
            alignItems: "center",
            textAlign: "center"
        }}>
            <h1 >RPG Music Companion</h1>
            {
                connected ?
                    <p style={{ marginBottom: "1rem" }}>{connected ? 'Connected' : 'Disconnected'}</p> :
                    <button style={{ marginBottom: "1rem" }} onClick={reconnect}>Reconnect</button>
            }
        </div >
    );
}

export default Header;