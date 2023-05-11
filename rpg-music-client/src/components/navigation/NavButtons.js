const NavButtons = ({ sendNavMessage }) => {
    return (
        <div
            style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr  ",
                gap: "1rem",
                justifyContent: "center",
            }}>
            <button onClick={() => sendNavMessage("NEXT_SECTION")}>Next Section</button>
            {/* <button onClick={() => sendNavMessage("NEXT_MOVEMENT")}>Next Movement</button> */}
        </div>
    );
}

export default NavButtons;