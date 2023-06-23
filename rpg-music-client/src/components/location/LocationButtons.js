import { CombatButton, LocationButton } from "../styled/Buttons";
import { CentredFlexColumn, TwoColGrid } from "../styled/Layouts";

const LocationButtons = ({ sendLocationMessage }) => {

    return (
        <>

            <CentredFlexColumn>
                <TwoColGrid>
                    <LocationButton onClick={() => sendLocationMessage("Abandoned_Mine")}>Abandoned Mine</LocationButton>
                    <LocationButton onClick={() => sendLocationMessage("Enchanted_Forest")}>Enchanted Forest</LocationButton>
                </TwoColGrid>
                <CombatButton onClick={() => sendLocationMessage("Combat")}>Combat</CombatButton>
            </CentredFlexColumn>
        </>
    );
}

export default LocationButtons;