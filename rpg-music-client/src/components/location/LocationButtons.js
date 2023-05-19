const LocationButtons = ({ sendLocationMessage}) => {

    return (
        <div
            style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr  ",
                gap: "1rem",
                justifyContent: "center",
            }}>
            <button onClick={() => sendLocationMessage("Combat")}>Combat</button>
            <button onClick={() => sendLocationMessage("Abandoned_Mine")}>Abandoned Mine</button>
            <button onClick={() => sendLocationMessage("Enchanted_Forest")}>Enchanted Forest</button>
        </div>);
}

export default LocationButtons;