import d20 from '../../images/d20.png';
import { CombatButton } from '../styled/Buttons';
import { Logo } from '../styled/Images';
import { HeaderDiv } from '../styled/Layouts';

const Header = ({ connected, reconnect }) => {
    return (
        <HeaderDiv>
            <Logo style={{ justifySelf: 'start', alignSelf: 'center', paddingLeft: "2rem" }} src={d20} alt="D20" />
            <h1 style={{ justifySelf: 'center', alignSelf: 'center', color: "white" }} >RPG Music Companion</h1>
            <div style={{ justifySelf: 'end', alignSelf: 'center', paddingRight: "2rem" }}>
                {
                    connected ?
                        <p style={{ color: "white" }}>
                            {connected ? 'Connected' : 'Disconnected'} 🟢</p> :
                        <p><CombatButton onClick={reconnect}>Reconnect</CombatButton>  🔴</p>
                }

            </div>
        </HeaderDiv>
    );
}

export default Header;